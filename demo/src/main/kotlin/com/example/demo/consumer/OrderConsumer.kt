package com.example.demo.consumer

import com.example.demo.exceptions.TemporaryException
import com.example.demo.retry.RetryRegistry
import com.example.demo.usecase.OrderService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.clients.producer.ProducerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class OrderConsumer(
    private val orderService: OrderService,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val retryRegistry: RetryRegistry
) {

    @KafkaListener(topics = ["orders"])
    fun consume(record: ConsumerRecord<String, String>) {

        val key = record.key()

        // PADRÃO: Manter ordem de eventos redirecionados
        // Se a chave já está em retry, enviamos as novas mensagens diretamente para o tópico de retry
        if (retryRegistry.contains(key)) {
            sendToRetry(record, getRetryCount(record))
            return
        }

        try {
            orderService.process(record.value())
        } catch (ex: TemporaryException) {
            // PADRÃO: Adicionar tópico de retry e aplicação de retry
            // Redireciona para o tópico de retry em caso de falha temporária
            retryRegistry.add(key)
            sendToRetry(record, 0)
        }
    }

    private fun sendToRetry(record: ConsumerRecord<String, String>, retryCount: Int) {
        val producerRecord = ProducerRecord<String, String>("orders.retry", record.key(), record.value())
        producerRecord.headers().add("x-retry-count", retryCount.toString().toByteArray())
        kafkaTemplate.send(producerRecord)
    }

    private fun getRetryCount(record: ConsumerRecord<String, String>): Int {
        val header = record.headers().lastHeader("x-retry-count")
        return header?.value()?.toString(Charsets.UTF_8)?.toInt() ?: 0
    }

}
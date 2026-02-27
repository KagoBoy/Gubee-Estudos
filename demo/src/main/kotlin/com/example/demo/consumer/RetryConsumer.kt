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
class RetryConsumer(
    private val orderService: OrderService,
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val retryRegistry: RetryRegistry
) {

    @KafkaListener(topics = ["orders.retry"])
    fun retry(record: ConsumerRecord<String, String>){
        // PADRÃO: Aplicação de Retry
        // Consome mensagens do tópico de retry e tenta reprocessar com backoff manual
        val key = record.key()
        val retryCount = getRetryCount(record)

        try {
            // Backoff manual para o padrão de retry
            Thread.sleep(2000)

            orderService.process(record.value())
            retryRegistry.remove(key)
        } catch (ex: TemporaryException)  {
            if (retryCount >= 5) {
                // PADRÃO: DLQ (após exaustão do retry manual)
                println("Limite de retries excedido para a chave $key. Enviando para DLQ.")
                kafkaTemplate.send("orders.DLQ", key, record.value())
                retryRegistry.remove(key)
            } else {
                sendToRetry(record, retryCount + 1)
            }
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
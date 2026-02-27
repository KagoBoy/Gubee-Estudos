package com.example.demo.consumer

import com.example.demo.usecase.OrderService
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.springframework.kafka.annotation.KafkaListener
import org.springframework.stereotype.Component

@Component
class StopOnErrorConsumer(
    private val orderService: OrderService
) {

    @KafkaListener(
        topics = ["orders.critical"],
        containerFactory = "stopOnErrorContainerFactory"
    )
    fun consume(record: ConsumerRecord<String, String>) {
        // PADRÃO: Stop on Error
        // Este consumer usa uma factory configurada para parar o container em caso de falha
        println("Recebido no tópico critical: ${record.value()}")
        orderService.process(record.value())
    }
}

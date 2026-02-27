package com.example.demo.producer

import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Service

@Service
class OrderProducer(private val kafkaTemplate: KafkaTemplate<String, String>) {

    fun send (orderId: String, payload: String) {
        kafkaTemplate.send("orders", orderId, payload)
    }

    fun sendStop (orderId: String, payload: String) {
        kafkaTemplate.send("orders.critical", orderId, payload)
    }

}
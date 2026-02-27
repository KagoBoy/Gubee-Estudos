package com.example.demo.controller

import com.example.demo.producer.OrderProducer
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/orders")
class SendMessage(
    private val orderProducer: OrderProducer
) {

    @PostMapping("/{id}")
    fun sendOrder(@PathVariable id: String,
                  @RequestBody payload: String): ResponseEntity<String> {
        orderProducer.send(id, payload)
        return ResponseEntity.ok("Order has been sent to $id")
    }

    @PostMapping("stopOnError/{id}")
    fun sendOrderStop(@PathVariable id: String,
                  @RequestBody payload: String): ResponseEntity<String> {
        orderProducer.sendStop(id, payload)
        return ResponseEntity.ok("Order has been sent to $id")
    }
}
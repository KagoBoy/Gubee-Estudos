package com.example.demo.usecase

import com.example.demo.exceptions.CriticalException
import com.example.demo.exceptions.PermanentException
import com.example.demo.exceptions.TemporaryException
import com.example.demo.exceptions.ValidationException
import org.springframework.stereotype.Service

@Service
class OrderService {

    fun process(order: String) {
        when {
            order.contains("PERM") -> {
                println("Pedido com erro permanente")
                throw PermanentException("Permanent exception")
            }
            order.contains("TEMP") -> {
                println("Pedido com erro temporario")
                throw TemporaryException("Temporary exception")
            }
            order.contains("INVALID") -> {
                println("Pedido com erro de validação")
                throw ValidationException("Validation exception")
            }
            order.contains("STOP") -> {
                println("ERRO CRÍTICO: Parando o processamento do tópico!")
                throw CriticalException("Critical failure - stop on error pattern triggered")
            }
        }

        println("Pedido processado com sucesso: $order")
    }

}
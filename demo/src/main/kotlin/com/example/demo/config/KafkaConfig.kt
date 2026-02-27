package com.example.demo.config

import com.example.demo.exceptions.CriticalException
import com.example.demo.exceptions.PermanentException
import com.example.demo.exceptions.ValidationException
import org.apache.kafka.common.TopicPartition
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory
import org.springframework.kafka.core.ConsumerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer
import org.springframework.kafka.listener.DefaultErrorHandler
import org.springframework.kafka.listener.CommonContainerStoppingErrorHandler
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries
import org.springframework.util.backoff.ExponentialBackOff
import org.springframework.util.backoff.FixedBackOff

@Configuration
class KafkaConfig(private val kafkaTemplate: KafkaTemplate<String, String>) {

    @Bean
    fun errorHandler(): DefaultErrorHandler {
        // PADRÃO: DLQ (Dead Letter Queue)
        // Configura o recoverer para enviar mensagens para o tópico .DLQ após esgotar tentativas
        val recoverer = DeadLetterPublishingRecoverer(kafkaTemplate) { record, _ ->
            TopicPartition("orders.DLQ", record.partition())
        }

        val backOff = ExponentialBackOffWithMaxRetries(3).apply {
            initialInterval = 2000L
            multiplier = 2.0
            maxInterval = 10000
        }

        val errorHandler = DefaultErrorHandler(recoverer, backOff)

        errorHandler.addNotRetryableExceptions(
            PermanentException::class.java,
            ValidationException::class.java
        )

        return errorHandler
    }

    @Bean
    fun stopOnErrorErrorHandler(): CommonContainerStoppingErrorHandler {
        // PADRÃO: Stop on Error
        // Este Error Handler interrompe o consumo do tópico imediatamente em caso de erro crítico
        return CommonContainerStoppingErrorHandler()
    }

    @Bean
    fun kafkaListenerContainerFactory(
        consumerFactory: ConsumerFactory<String, String>,
        errorHandler: DefaultErrorHandler
    ): ConcurrentKafkaListenerContainerFactory<String, String> {

        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory
        factory.setCommonErrorHandler(errorHandler)

        return factory
    }

    @Bean
    fun stopOnErrorContainerFactory(
        consumerFactory: ConsumerFactory<String, String>,
        stopOnErrorErrorHandler: CommonContainerStoppingErrorHandler
    ): ConcurrentKafkaListenerContainerFactory<String, String> {

        val factory = ConcurrentKafkaListenerContainerFactory<String, String>()
        factory.consumerFactory = consumerFactory
        factory.setCommonErrorHandler(stopOnErrorErrorHandler)

        return factory
    }

}
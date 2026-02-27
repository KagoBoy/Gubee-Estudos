package com.example.demo.retry

import org.springframework.stereotype.Component
import java.util.concurrent.ConcurrentHashMap

@Component
class RetryRegistry {
    private val retryingKeys = ConcurrentHashMap.newKeySet<String>()

    fun add(key: String) = retryingKeys.add(key)
    fun remove(key: String) = retryingKeys.remove(key)
    fun contains(key: String): Boolean = retryingKeys.contains(key)
}

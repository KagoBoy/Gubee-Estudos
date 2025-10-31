package org.acme;

import java.util.concurrent.atomic.AtomicInteger;

import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class RequestCounter {

    private AtomicInteger concurrentExecutions = new AtomicInteger(0);
    private AtomicInteger totalRequests = new AtomicInteger(0);
    
    public int incrementAndGetConcurrent() {
        return concurrentExecutions.incrementAndGet();
    }
    
    public int decrementAndGetConcurrent() {
        return concurrentExecutions.decrementAndGet();
    }
    
    public int incrementAndGetTotal() {
        return totalRequests.incrementAndGet();
    }
    
    public int getCurrentConcurrent() {
        return concurrentExecutions.get();
    }
}

package org.acme;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.jboss.logging.Logger;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/breaker")
public class CircuitBreakerResource {
    private static final Logger LOG = Logger.getLogger(CircuitBreakerResource.class);
    private AtomicInteger requestCount = new AtomicInteger(0);
    private AtomicInteger failureCount = new AtomicInteger(0);
    private Random random = new Random();

    @CircuitBreaker(
        requestVolumeThreshold = 5,     // Analisa as últimas 5 requisições
        failureRatio = 0.8,             // Abre circuito se 80% falharem
        delay = 10000,                  // Fica aberto por 10 segundos
        successThreshold = 2            // Precisa de 2 sucessos para fechar
    )
    @Fallback(fallbackMethod = "circuitBreakerFallback")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String circuitBreakerBasic() {
        int count = requestCount.incrementAndGet();
        LOG.info("Requisição " + count + " - Circuito FECHADO");
        
        if (random.nextDouble() < 0.4) {
            failureCount.incrementAndGet();
            LOG.info("Contador de falhas: " + failureCount);
            LOG.error("Requisição " + count + " - FALHA");
            throw new RuntimeException("Erro simulado na requisição " + count);
        }
        
        LOG.info("Requisição " + count + " - SUCESSO");
        return "Sucesso na requisição " + count;
    }

    public String circuitBreakerFallback() {
        LOG.warn("Circuit Breaker Fallback acionado");
        return "Serviço temporariamente indisponível - Modo fallback";
    }
}

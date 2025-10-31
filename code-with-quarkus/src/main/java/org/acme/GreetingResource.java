package org.acme;

import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.logging.Logger;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    private static final Logger LOG = Logger.getLogger(GreetingResource.class);
    private AtomicInteger counter = new AtomicInteger(0);

    @Retry(maxRetries = 3, delay = 1000)
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        int attempt = counter.incrementAndGet();
        LOG.info("Tentativa número: " + attempt);
        
        if (attempt <= 2) {
            LOG.error("Falha intencional na tentativa " + attempt);
            throw new RuntimeException("Falha temporária na busca do produto");
        }
        
        LOG.info("Sucesso na tentativa " + attempt);
        return "Hello from Quarkus REST - Sucesso na tentativa " + attempt;
    }
}

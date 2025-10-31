package org.acme;

import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.jboss.logging.Logger;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/bulkhead")
@ApplicationScoped
public class BulkheadResource {

    private static final Logger LOG = Logger.getLogger(BulkheadResource.class);
    
    @Inject
    RequestCounter counter;

    @Bulkhead(3) // permite apenas 3 execuções simultâneas
    @Fallback(fallbackMethod = "bulkheadFallback")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String bulkheadSync() {
        int currentConcurrent = counter.incrementAndGetConcurrent();
        int requestId = counter.incrementAndGetTotal();
        
        LOG.info("REQUISIÇÃO " + requestId + " INICIADA - Concorrentes: " + currentConcurrent);
        
        try {
            Thread.sleep(1000);
            return "Requisição " + requestId + " CONCLUÍDA - Concorrentes: " + currentConcurrent;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return "Requisição " + requestId + " INTERROMPIDA";
        } finally {
            counter.decrementAndGetConcurrent();
            LOG.info("Requisição " + requestId + " FINALIZADA");
        }
    }

    public String bulkheadFallback() {
        LOG.warn("BULKHEAD LOTADO - Fallback acionado");
        return "Serviço ocupado - Muitas requisições simultâneas. Tente novamente.";
    }
}

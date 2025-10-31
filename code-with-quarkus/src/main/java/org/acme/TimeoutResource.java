package org.acme;


import org.eclipse.microprofile.faulttolerance.Timeout;
import org.jboss.logging.Logger;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;


@Path("/timeout")
public class TimeoutResource {

    //requisicao não retorna processo concluido porque passa o tempo de timeout
    

    private static final Logger LOG = Logger.getLogger(GreetingResource.class);

    @Timeout(1000)
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String timeoutTest() {
        LOG.info("=== INICIANDO PROCESSO LONGO ===");
        try {
            Thread.sleep(3000);
            return "Processo concluído com sucesso!";
        } catch (InterruptedException e) {
            LOG.error("=== TIMEOUT OCORREU ===");
            Thread.currentThread().interrupt();
            return "Processo interrompido por timeout";
        }
    }
}

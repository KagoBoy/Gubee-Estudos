package org.acme;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.jboss.logging.Logger;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/fallback")
public class FallBackResource {

    //simulando erro no serviço simulatingError que chama o metódo fallBack()

    private static final Logger LOG = Logger.getLogger(GreetingResource.class);

    @Fallback(fallbackMethod = "fallBack")
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String simulatingError() {
        LOG.info("Entrou na requisição com falha!");
        throw new RuntimeException("Falha temporária na busca do produto");
        //return "Retorno do serviço principal";
    }

    
    public String fallBack() {
        LOG.info("Fallback acionado!");
        return "Retorno do fallback - Serviço principal indisponível";
    }

}

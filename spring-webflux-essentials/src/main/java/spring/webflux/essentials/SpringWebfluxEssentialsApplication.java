package spring.webflux.essentials;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import reactor.blockhound.BlockHound;

@SpringBootApplication(exclude = {UserDetailsServiceAutoConfiguration.class})
public class SpringWebfluxEssentialsApplication {

	static {
		BlockHound.install(builder -> builder.allowBlockingCallsInside("java.util.UUID", "randomUUID")
				.allowBlockingCallsInside("java.io.InputStream", "readNBytes")
				.allowBlockingCallsInside("java.io.FilterInputStream", "read")


				//allow spring doc
				.allowBlockingCallsInside("org.springdoc.core.service.OpenAPIService", "initializeHiddenRestController")
				.allowBlockingCallsInside("org.springdoc.core.service.OpenAPIService", "build")
				.allowBlockingCallsInside("org.springdoc.api.AbstractOpenApiResource", "getOpenApi")
				.allowBlockingCallsInside("java.util.stream.ReferencePipeline", "collect")
				.allowBlockingCallsInside("java.util.stream.ReduceOps", "evaluateParallel")
		);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringWebfluxEssentialsApplication.class, args);
	}

}

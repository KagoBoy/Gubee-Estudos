package spring.webflux.essentials.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import spring.webflux.essentials.service.UserDetailsService;

@Configuration
@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        //@formatter: off
        return http
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .authorizeExchange(exchanges -> exchanges
                    .pathMatchers(HttpMethod.POST, "animes/**").hasRole("ADMIN")
                    .pathMatchers(HttpMethod.PUT, "animes/**").hasRole("ADMIN")
                    .pathMatchers(HttpMethod.DELETE, "animes/**").hasRole("ADMIN")
                    .pathMatchers(HttpMethod.GET,  "animes/**").hasAnyRole("USER", "ADMIN")
                                .pathMatchers("/swagger-ui.html", "/webjars/**", "/v3/api-docs/**").permitAll()
                .anyExchange().authenticated()
                )
                .formLogin(formLoginSpec -> {})
                .httpBasic(httpBasicSpec -> {})
                .build();

    }


    //Reactive Security In Memory
//    @Bean
//    public MapReactiveUserDetailsService userDetailsService() {
//        PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
//        UserDetails user = User.withUsername("user")
//                .password(passwordEncoder.encode("yan123"))
//                .roles("USER")
//                .build();
//
//        UserDetails admin = User.withUsername("admin")
//                .password(passwordEncoder.encode("yan123"))
//                .roles("USER", "ADMIN")
//                .build();
//
//        return new MapReactiveUserDetailsService(user, admin);
//    }


    //Reactive Security com banco de Dados
    @Bean
    ReactiveAuthenticationManager authenticationManager(UserDetailsService userDetailsService){
        return new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
    }
}

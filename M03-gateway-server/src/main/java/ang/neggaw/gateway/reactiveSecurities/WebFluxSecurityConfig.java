package ang.neggaw.gateway.reactiveSecurities;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.MapReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author ANG
 * @since 12-07-2021 14:46
 */

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
@Log4j2
public class WebFluxSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) throws JsonProcessingException {

        ObjectMapper objectMapper = new ObjectMapper();

        return http
                .csrf().disable()
                .httpBasic().and()
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
                            log.error("authenticationEntryPoint: {} {}. Error message: {}", swe.getRequest().getRemoteAddress(), swe.getRequest().getPath(), e.getMessage());
                            swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                            try {
                                String body = objectMapper.writeValueAsString(new MyHttpResponse(HttpStatus.UNAUTHORIZED.value(), e.getMessage(), e));
                                swe.getResponse().writeWith(Mono.just(swe.getResponse().bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8))));
                            } catch (JsonProcessingException jsonProcessingException) {
                                jsonProcessingException.printStackTrace();
                            }
                        })
                ).accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
                    log.error("accessDeniedHandler: {} {}. Error message: {}", swe.getRequest().getRemoteAddress(), swe.getRequest().getPath(), e.getMessage());
                    swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                }))
                .and()
                .authorizeExchange()
                    .pathMatchers(HttpMethod.OPTIONS).permitAll()
                    .pathMatchers("/user-microservice/auth/login", "/actuator/**").permitAll()
                    .pathMatchers(authenticatedMicroservicesUrl()).permitAll()
                    .anyExchange().authenticated().and()
                .headers().frameOptions().mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN).and()
                .build();
    }

    private static String[] authenticatedMicroservicesUrl() {
        return new String[]{
            "/user-microservice/**",
            "/city-microservice/**",
            "/cinema-microservice/**",
            "/movie-microservice/**",
            "/ticket-microservice/**",
            "/swagger-ui/**"
        };
    }

    @Bean
    public MapReactiveUserDetailsService mapReactiveUserDetailsService () {
        //PasswordEncoder password = PasswordEncoderFactories.createDelegatingPasswordEncoder();
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

        UserDetails user = User.withUsername("user")
                .password(passwordEncoder.encode("gateway"))
                .roles("USER")
                .build();
        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("gateway"))
                .roles("ADMIN", "USER")
                .build();
        return new MapReactiveUserDetailsService(user, admin);
    }
}
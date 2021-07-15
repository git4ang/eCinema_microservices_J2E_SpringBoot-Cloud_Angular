package ang.neggaw.users.reactiveSecurities;

import ang.neggaw.users.reactiveSecurities.filters.JwtWebFilter;
import ang.neggaw.users.reactiveSecurities.filters.SecurityContextRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;

/**
 * @author ANG
 * @since 12-07-2021 18:03
 */

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
@Configuration
@Log4j2
public class WebFluxSecurityConfig {

    private final SecurityContextRepository securityContextRepository;

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http,
                                                         JwtWebFilter jwtWebFilter) {
        return http
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().and()
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> {
                    swe.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                    try {
                        errorExceptionHandling(swe, e).subscribe();
                    } catch (JsonProcessingException eJson) {
                        eJson.printStackTrace();
                    }
                    log.error("authenticationEntryPoint: {} {}. Error message: {}",
                            swe.getRequest().getRemoteAddress(), swe.getRequest().getPath(), e.getMessage());
                }))
                .accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> {
                    swe.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    try {
                        errorExceptionHandling(swe, e).subscribe();
                    } catch (JsonProcessingException eJson) {
                        eJson.printStackTrace();
                    }
                    log.error("accessDeniedHandler: {} {}. Error message: {}",
                            swe.getRequest().getRemoteAddress(), swe.getRequest().getPath(), e.getMessage());
                })).and()
                .authorizeExchange()
                    .pathMatchers(permittedUrl()).permitAll()
                    .pathMatchers(HttpMethod.OPTIONS).permitAll()
                    .pathMatchers(HttpMethod.GET, authenticatedUrl()).hasAnyAuthority("ADMIN", "USER")
                    .pathMatchers(HttpMethod.POST, authenticatedUrl()).hasAnyAuthority("ADMIN")
                    .pathMatchers(HttpMethod.PUT, authenticatedUrl()).hasAnyAuthority("ADMIN")
                    .pathMatchers(HttpMethod.PATCH, authenticatedUrl()).hasAnyAuthority("ADMIN")
                    .pathMatchers(HttpMethod.DELETE, authenticatedUrl()).hasAnyAuthority("ADMIN")
                    .anyExchange().authenticated().and()
                .securityContextRepository(securityContextRepository)
                .addFilterAt(jwtWebFilter, SecurityWebFiltersOrder.FIRST)
                .headers().frameOptions().mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN).and()
                .build();
    }

    private static String[] permittedUrl() {
        return new String[]{
                "/css", "/js", "/images", "/webjars", "/favicon.ico", "/index", "/login", "/logout", "/home",
                "/auth/login", "/auth/me", "/h2/**", "/h2-console/**",
        };
    }

    private static String[] authenticatedUrl() {
        return new String[]{
                "/api/**",
                "/customers/**",
                "/roles/**",
                "/users/**"
        };
    }

    private Mono<Void> errorExceptionHandling(ServerWebExchange exchange, Exception e) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        exchange.getResponse().getHeaders().setContentType(MediaType.APPLICATION_JSON);
        String body = objectMapper.writeValueAsString(Map.of(
                "Error message", e.getMessage(),
                "Remote address", Objects.requireNonNull(exchange.getRequest().getRemoteAddress()),
                "Path", exchange.getRequest().getPath().value()));
        DataBuffer dataBuffer = exchange.getResponse().bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return exchange.getResponse().writeWith(Mono.just(dataBuffer));
    }
}
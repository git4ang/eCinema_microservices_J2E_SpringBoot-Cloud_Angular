package ang.neggaw.users.reactiveSecurities;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.header.XFrameOptionsServerHttpHeadersWriter;
import reactor.core.publisher.Mono;

/**
 * @author ANG
 * @since 12-07-2021 18:03
 */

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
@RequiredArgsConstructor
@Log4j2
public class WebFluxSecurityConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http
                .csrf().disable()
                .exceptionHandling()
                .authenticationEntryPoint((swe, e) -> Mono.fromRunnable(() -> log.error("authenticationEntryPoint: {} {}. Error message: {}",
                        swe.getRequest().getRemoteAddress(), swe.getRequest().getPath(), e.getMessage()))
                ).accessDeniedHandler((swe, e) -> Mono.fromRunnable(() -> log.error("accessDeniedHandler: {} {}. Error message: {}",
                        swe.getRequest().getRemoteAddress(), swe.getRequest().getPath(), e.getMessage())))
                .and()
                .authorizeExchange()
                .pathMatchers(permittedUrl()).permitAll()
                .pathMatchers(HttpMethod.OPTIONS).permitAll()
                .anyExchange().authenticated().and()
                .formLogin().disable()
                .httpBasic().disable()
                .logout().and()
                .headers().frameOptions().mode(XFrameOptionsServerHttpHeadersWriter.Mode.SAMEORIGIN).and()
                .build();
    }

    private static String[] permittedUrl() {
        return new String[]{
                "/css", "/js", "/images", "/webjars", "/favicon.ico", "/index", "/login", "/logout", "/home",
                "/actuator/**", "/api/customers/**", "/api/roles/**", "/api/users/**",
        };
    }
}
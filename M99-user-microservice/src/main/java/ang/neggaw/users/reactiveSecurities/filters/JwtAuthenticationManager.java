package ang.neggaw.users.reactiveSecurities.filters;

import ang.neggaw.users.reactiveSecurities.utils.JwtSigner;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author ANG
 * @since 13-07-2021 12:08
 */

@Component
@RequiredArgsConstructor
@Log4j2
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtSigner jwtSignerUser;

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {

        log.info("Accessing to ReactiveAuthenticationManager ...");
        return Mono.just(authentication)
                .map(auth -> jwtSignerUser.parseToken(auth.getCredentials().toString()))
                .log()
                .onErrorResume(e -> {
                    log.error("Error verification of token: {} with this error message: {}", e.getClass(), e.getMessage());
                    return Mono.empty();
                })
                .map(decodedJWT -> new UsernamePasswordAuthenticationToken(
                        decodedJWT.getSubject(),
                        null,
                        Stream.of(decodedJWT.getClaims().get(jwtSignerUser.getAuthoritiesTag()))
                                .peek(info -> log.info("authorities: {}", info))
                                .flatMap(roles -> Arrays.stream(roles.asArray(String.class)).map(SimpleGrantedAuthority::new))
                                .collect(Collectors.toList())
                ));
    }
}
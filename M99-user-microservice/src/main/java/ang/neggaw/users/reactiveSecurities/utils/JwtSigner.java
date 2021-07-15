package ang.neggaw.users.reactiveSecurities.utils;

import ang.neggaw.users.entities.UserReact;
import ang.neggaw.users.repositories.UserRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.Objects;

/**
 * @author ANG
 * @since 13-07-2021 11:56
 */

@Service
@RequiredArgsConstructor
@Log4j2
public class JwtSigner {

    private final UserRepository userRepository;

    private final String SECRET_KEY = "secret-phrase-jwt";
    private final String AUTHORITIES = "authorities";
    private final String ISSUER = "https://github.com/git4ang";
    private final String TOKEN_PREFIX = "Bearer ";
    private final Long EXPIRATION_IN = 60 * 60 * 1000L; // = 1 Hour
    private final Long VALIDATED_NOT_BEFORE = 1000L; // = 1 Seconde

    public String getAuthoritiesTag() { return AUTHORITIES; }
    public String getIssuerTag() { return ISSUER; }
    public String getTokenPrefix() { return TOKEN_PREFIX; }

    public boolean isTokenExpired(String authorization) {
        return parseToken(authorization.substring(TOKEN_PREFIX.length())).getExpiresAt().before(Date.from(Instant.now()));
    }

    private Map<String, Object> generateToken(String username) {
        return createAndGenerateToken(Objects.requireNonNull(userRepository.findByUsername(username).block()));
    }

    public Map<String, Object> createAndGenerateToken(UserReact userReact) {

        log.info("Creating tokenJWT ...");

        String token = JWT
                .create()
                .withSubject(userReact.getUsername())
                .withClaim(AUTHORITIES, userReact.getAuthorities().stream().toList())
                .withIssuer(ISSUER)
                .withIssuedAt(new Date())
                .withExpiresAt(Date.from(Instant.now().plus(Duration.ofMillis(EXPIRATION_IN))))
                .withNotBefore(Date.from(Instant.now().plus(Duration.ofMillis(VALIDATED_NOT_BEFORE))))
                .sign(Algorithm.HMAC512(SECRET_KEY));
        log.info("TokenJWT CREATED successfully.");

        return Map.of(
                "token_type", TOKEN_PREFIX.trim(),
                "access_token", TOKEN_PREFIX + token,
                "username", userReact.getUsername(),
                "authorities", userReact.getAuthorities().stream().toList(),
                "issuer", ISSUER,
                "expiration_in", new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(System.currentTimeMillis() + EXPIRATION_IN))
        );
    }

    public DecodedJWT parseToken(String token) {
        log.info("Token: {}", token.substring(0, 33) + " ...");
        JWTVerifier jwtVerifier = JWT
                .require(Algorithm.HMAC512(SECRET_KEY))
                .withIssuer(ISSUER)
                .build();
        return jwtVerifier.verify(token);
    }
}
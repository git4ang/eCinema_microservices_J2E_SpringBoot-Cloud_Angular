package ang.neggaw.cinemas.securities.services;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author ANG
 * @since 15-07-2021 20:00
 */

@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@Log4j2
@Service
public class JwtServiceImpl implements JwtService {

    public static final String PREFIX = "Bearer ";
    public static final String AUTHORITIES = "authorities";
    private static final String ISSUER = "https://github.com/git4ang";
    private static final String SECRET = "secret-phrase-jwt";

    @Override
    public String isBearer(String authorization) {
        if (authorization != null && authorization.startsWith(PREFIX) && authorization.split("\\.").length == 3)
            return authorization;
        return null;
    }

    @Override
    public DecodedJWT verifyToken(String authorization) {
        if( isBearer(authorization) == null)
            throw new JWTDecodeException("Not token JWT found or not exists.");
        try {
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC512(SECRET))
                    .withIssuer(ISSUER).build();
            return jwtVerifier.verify(isBearer(authorization).substring(PREFIX.length()));
        } catch (Exception e) {
            throw  new ResponseStatusException(HttpStatus.UNAUTHORIZED, e.getMessage());
        }
    }

    @Override
    public DecodedJWT decodeToken(String authorization) {

        DecodedJWT jwt = null;
        if (verifyToken(authorization) != null) {
            try {
                jwt = verifyToken(authorization);
                Collection<GrantedAuthority> authorities = new ArrayList<>();
                jwt.getClaims().get(AUTHORITIES).asList(String.class).forEach(roleName -> authorities.add(new SimpleGrantedAuthority(roleName)));

                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(jwt.getSubject(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            } catch (Exception e) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unable to decode token JWT. " + e.getMessage());
            }
        }
        return jwt;
    }
}
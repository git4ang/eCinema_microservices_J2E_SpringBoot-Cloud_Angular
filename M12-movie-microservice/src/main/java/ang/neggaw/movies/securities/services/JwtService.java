package ang.neggaw.movies.securities.services;

import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @author ANG
 * @since 15-07-2021 21:07
 */

public interface JwtService {
    String isBearer(String authorization);
    DecodedJWT verifyToken(String authorization);
    DecodedJWT decodeToken(String authorization);
}

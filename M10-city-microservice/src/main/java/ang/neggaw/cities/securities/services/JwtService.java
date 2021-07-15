package ang.neggaw.cities.securities.services;

import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @author ANG
 * @since 13-07-2021 11:15
 */

public interface JwtService {
    String isBearer(String authorization);
    DecodedJWT verifyToken(String authorization);
    DecodedJWT decodeToken(String authorization);
}

package ang.neggaw.tickets.securities.services;

import com.auth0.jwt.interfaces.DecodedJWT;

/**
 * @author ANG
 * @since 15-07-2021 21:22
 */

public interface JwtService {
    String isBearer(String authorization);
    DecodedJWT verifyToken(String authorization);
    DecodedJWT decodeToken(String authorization);
}

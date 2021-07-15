package ang.neggaw.users.reactiveSecurities.filters;

import ang.neggaw.users.reactiveSecurities.models.MyHttpResponse;
import ang.neggaw.users.reactiveSecurities.utils.JwtSigner;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;

/**
 * @author ANG
 * @since 13-07-2021 11:46
 */

@Component
@Log4j2
@RequiredArgsConstructor
public class JwtWebFilter implements WebFilter {

    private final JwtSigner jwtSignerUser;

    protected Mono<Void> getErrorMessage(ServerHttpResponse response,
                                         HttpStatus httpStatus,
                                         String message) throws JsonProcessingException {

        response.getHeaders().setContentType(MediaType.APPLICATION_JSON);
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(new MyHttpResponse(httpStatus.value(), message, null));
        DataBuffer dataBuffer = response.bufferFactory().wrap(body.getBytes(StandardCharsets.UTF_8));
        return response.writeWith(Mono.just(dataBuffer));
    }

    @SneakyThrows
    @Override
    public Mono<Void> filter(ServerWebExchange serverWebExchange, WebFilterChain webFilterChain) {

        ServerHttpResponse response = serverWebExchange.getResponse();
        ServerHttpRequest request = serverWebExchange.getRequest();

        String path = request.getPath().value();
        if( path.contains("/auth/login")  || path.contains("/auth/logout"))
            return webFilterChain.filter(serverWebExchange);

        String authorization = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
        if(authorization == null) {
            response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
            return getErrorMessage(response, HttpStatus.NOT_ACCEPTABLE, "TokenJWT Not Found or Not exist");
        }

        else {
            if( ! authorization.startsWith(jwtSignerUser.getTokenPrefix())) {
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
                return getErrorMessage(response, HttpStatus.NOT_ACCEPTABLE, "TokenJWT Not starts with '" + jwtSignerUser.getTokenPrefix());
            }

            else if( authorization.split("\\.").length != 3) {
                response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
                return getErrorMessage(response, HttpStatus.NOT_ACCEPTABLE, "TokenJWT with bad format");
            }

            else {
                try {
                    if( jwtSignerUser.isTokenExpired(authorization)) {
                        response.setStatusCode(HttpStatus.NOT_ACCEPTABLE);
                        return getErrorMessage(response, HttpStatus.NOT_ACCEPTABLE, "The token is expired");
                    }

                    else {
                        String token = authorization.substring(jwtSignerUser.getTokenPrefix().length());
                        serverWebExchange.getAttributes().put("token", token);
                    }

                } catch (Exception e) {
                    response.setStatusCode(HttpStatus.INTERNAL_SERVER_ERROR);
                    return getErrorMessage(response, HttpStatus.INTERNAL_SERVER_ERROR, e.getMessage().replace("\"", ""));
                }
            }
        }

        return webFilterChain.filter(serverWebExchange);
    }
}

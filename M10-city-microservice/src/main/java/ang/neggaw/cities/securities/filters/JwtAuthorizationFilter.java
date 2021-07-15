package ang.neggaw.cities.securities.filters;

import ang.neggaw.cities.securities.models.MyHttpResponse;
import ang.neggaw.cities.securities.services.JwtServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ANG
 * @since 13-07-2021 11:10
 */

@Log4j2
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    protected void getErrorMessage(HttpServletResponse response,
                                   HttpStatus httpStatus,
                                   String message) throws IOException {

        //response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON.toString());
        ObjectMapper objectMapper = new ObjectMapper();
        String body = objectMapper.writeValueAsString(new MyHttpResponse(httpStatus.value(), message, null));
        response.setStatus(httpStatus.value());
        response.getOutputStream().println(body);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        JwtServiceImpl jwtService = new JwtServiceImpl();

        if(request.getServletPath().startsWith("/actuator")) {
            filterChain.doFilter(request, response);
            return;
        }

        log.info("Fetching token JWT in Headers...");
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(authorization == null) {
            log.error("{}: TokenJWT Not Found or Not exist", HttpStatus.NOT_ACCEPTABLE);
            getErrorMessage(response, HttpStatus.NOT_ACCEPTABLE, "TokenJWT Not Found or Not exist");
            return;
        }
        else {

            if( ! authorization.startsWith(JwtServiceImpl.PREFIX)) {
                log.error("{}: TokenJWT Not starts with: '{}'", HttpStatus.NOT_ACCEPTABLE, JwtServiceImpl.PREFIX);
                getErrorMessage(response, HttpStatus.NOT_ACCEPTABLE, "TokenJWT Not starts with '" + JwtServiceImpl.PREFIX);
                return;
            }
            else if( authorization.split("\\.").length != 3) {
                log.error("{}: TokenJWT with bad format", HttpStatus.NOT_ACCEPTABLE);
                getErrorMessage(response, HttpStatus.NOT_ACCEPTABLE, "TokenJWT with bad format");
                return;
            }
            else {
                try {
                    if (jwtService.verifyToken(authorization) != null) {
                        jwtService.decodeToken(authorization);
                        log.info("Token JWT DECODED successfully");
                    }
                } catch (Exception e) {
                    getErrorMessage(response, HttpStatus.UNAUTHORIZED, e.getMessage().replace("\"", ""));
                    log.error(e.getMessage());
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}

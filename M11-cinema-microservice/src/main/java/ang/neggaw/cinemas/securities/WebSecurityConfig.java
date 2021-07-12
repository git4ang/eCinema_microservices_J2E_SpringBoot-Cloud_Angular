package ang.neggaw.cinemas.securities;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandlerImpl;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ANG
 * @since 12-07-2021 16:10
 */

@Order(1)
@Log4j2
@Configuration
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.csrf().disable()
                .cors(request -> new CorsConfiguration().applyPermitDefaultValues())
                .exceptionHandling()
                .authenticationEntryPoint((req, resp, e) -> {
                    resp.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
                    log.error(e.getMessage());
                }).and()
                .exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl() {
            @Override
            public void handle(HttpServletRequest request,
                               HttpServletResponse response,
                               AccessDeniedException e) throws IOException, ServletException {
                response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
                log.error(e.getMessage());
            }
        }).and()
                .headers()
                .frameOptions()
                .sameOrigin().and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.OPTIONS).permitAll()
                    .antMatchers(permittedUrl()).permitAll()
                    .anyRequest().authenticated().and()
                .logout().deleteCookies("JSESSIONID");
    }

    private static String[] permittedUrl() {
        return new String[]{
                "/css", "/js", "/images", "/webjars", "/favicon.ico", "/index", "/login", "/logout", "/home",
                "/actuator/**", "/h2/**", "/h2-console/**", "/swagger-ui.html", "/api-docs", "/api/cinemas/**", "/api/rooms/**", "/api/seats/**",
        };
    }
}
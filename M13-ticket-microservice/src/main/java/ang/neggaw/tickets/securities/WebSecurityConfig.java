package ang.neggaw.tickets.securities;

import ang.neggaw.tickets.securities.filters.JwtAuthorizationFilter;
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
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author ANG
 * @since 12-07-2021 16:15
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
                .authenticationEntryPoint((request, response, e) -> {
                    log.error("Error: {}. Message: {}. Path: '{}'.", HttpStatus.UNAUTHORIZED, e.getMessage(), request.getServletPath());
                    response.sendError(HttpStatus.UNAUTHORIZED.value(), e.getMessage());
                }).and()
                .exceptionHandling().accessDeniedHandler(new AccessDeniedHandlerImpl() {
            @Override
            public void handle(HttpServletRequest request,
                               HttpServletResponse response,
                               AccessDeniedException e) throws IOException, ServletException {
                log.error("Code: {}. Message: {}. Path: '{}'.", HttpStatus.FORBIDDEN, e.getMessage(), request.getServletPath());
                response.sendError(HttpStatus.FORBIDDEN.value(), e.getMessage());
            }
        }).and()
        .headers()
        .frameOptions()
        .sameOrigin().and()
        .authorizeRequests()
            .antMatchers(HttpMethod.OPTIONS).permitAll()
            .antMatchers(permittedUrl()).permitAll()
            .antMatchers(HttpMethod.GET, authenticatedUrl()).hasAnyAuthority("ADMIN", "USER")
            .antMatchers(HttpMethod.POST, authenticatedUrl()).hasAnyAuthority("ADMIN")
            .antMatchers(HttpMethod.PUT, authenticatedUrl()).hasAnyAuthority("ADMIN")
            .antMatchers(HttpMethod.PATCH, authenticatedUrl()).hasAnyAuthority("ADMIN")
            .antMatchers(HttpMethod.DELETE, authenticatedUrl()).hasAnyAuthority("ADMIN")
            .anyRequest().authenticated().and()
        .addFilterBefore(new JwtAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class)

        .logout().deleteCookies("JSESSIONID");
    }

    private static String[] permittedUrl() {
        return new String[]{
                "/css", "/js", "/images", "/webjars", "/favicon.ico", "/index", "/login", "/logout", "/home",
                "/h2/**", "/h2-console/**", "/actuator/**", "/open-api/**",
        };
    }

    private static String[] authenticatedUrl() {
        return new String[] {
                "/api/tickets/**",
                "/tickets/**",
        };
    }
}
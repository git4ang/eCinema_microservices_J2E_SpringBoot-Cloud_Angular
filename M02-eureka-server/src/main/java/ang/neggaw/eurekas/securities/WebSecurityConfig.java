package ang.neggaw.eurekas.securities;

import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author ANG
 * @since  12-07-2021 13:38
 */

@EnableWebSecurity
@Order(value = Ordered.HIGHEST_PRECEDENCE)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final BCryptPasswordEncoder passwordEncoder;

    public WebSecurityConfig(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {

        auth.inMemoryAuthentication()
                .withUser("admin")
                .password(passwordEncoder.encode("eureka"))
                .roles("SYSTEM", "ADMIN");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
            .csrf().disable()
            .httpBasic().and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.ALWAYS).and()
            .requestMatchers()
            .antMatchers("/eureka/**").and()
            .authorizeRequests()
                .antMatchers("/eureka/**")
                .hasRole("SYSTEM")
                .anyRequest()
                .denyAll().and()
            .httpBasic();
    }

    @Configuration
    public static class AdminSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication();
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {

            http
                .csrf().disable()
                .httpBasic().disable()
                .sessionManagement()
                    .sessionCreationPolicy(SessionCreationPolicy.NEVER).and()
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET, "/**").hasRole("ADMIN")
                    .antMatchers("/info", "/health")
                    .authenticated()
                    .anyRequest()
                    .denyAll();
        }
    }
}
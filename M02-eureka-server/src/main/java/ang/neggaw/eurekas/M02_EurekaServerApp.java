package ang.neggaw.eurekas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@EnableEurekaServer
@SpringBootApplication
public class M02_EurekaServerApp {
    public static void main(String[] args) {
        SpringApplication.run(M02_EurekaServerApp.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}

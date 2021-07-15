package ang.neggaw.cinemas;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@OpenAPIDefinition
@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class M11_CinemaMicroserviceApp {
    public static void main(String[] args) {
        SpringApplication.run(M11_CinemaMicroserviceApp.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}

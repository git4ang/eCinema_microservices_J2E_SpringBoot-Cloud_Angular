package ang.neggaw.movies;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@OpenAPIDefinition
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class M12_MovieMicroserviceApp {
    public static void main(String[] args) {
        SpringApplication.run(M12_MovieMicroserviceApp.class, args);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }
}

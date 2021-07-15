package ang.neggaw.tickets;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@OpenAPIDefinition
@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class M13_TicketMicroserviceApp {
    public static void main(String[] args) {
        SpringApplication.run(M13_TicketMicroserviceApp.class, args);
    }
}

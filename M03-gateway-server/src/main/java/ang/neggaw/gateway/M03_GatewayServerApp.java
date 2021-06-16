package ang.neggaw.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class M03_GatewayServerApp {
    public static void main(String[] args) {
        SpringApplication.run(M03_GatewayServerApp.class, args);
    }
}

package ang.neggaw.eurekas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class M02_EurekaServerApp {
    public static void main(String[] args) {
        SpringApplication.run(M02_EurekaServerApp.class, args);
    }
}

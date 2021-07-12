package ang.neggaw.admins;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@EnableAdminServer
@SpringBootApplication
public class M04_AdminServerApp {
    public static void main(String[] args) {
        SpringApplication.run(M04_AdminServerApp.class, args);
    }
}

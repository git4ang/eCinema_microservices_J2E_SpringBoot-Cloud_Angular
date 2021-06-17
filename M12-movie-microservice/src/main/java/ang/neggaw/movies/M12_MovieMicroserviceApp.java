package ang.neggaw.movies;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class M12_MovieMicroserviceApp {
    public static void main(String[] args) {
        SpringApplication.run(M12_MovieMicroserviceApp.class, args);
    }
}

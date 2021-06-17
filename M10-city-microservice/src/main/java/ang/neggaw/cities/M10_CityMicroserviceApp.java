package ang.neggaw.cities;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class M10_CityMicroserviceApp {
    public static void main(String[] args) {
        SpringApplication.run(M10_CityMicroserviceApp.class, args);
    }
}

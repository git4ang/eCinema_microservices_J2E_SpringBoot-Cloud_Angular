package ang.neggaw.cities;

import ang.neggaw.cities.entities.City;
import ang.neggaw.cities.repositories.CityRepository;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class M10_CityMicroserviceApp {
    public static void main(String[] args) {
        SpringApplication.run(M10_CityMicroserviceApp.class, args);
    }

    @Bean
    ApplicationRunner runner(RepositoryRestConfiguration restConfiguration,
                             CityRepository cityRepository) {

        restConfiguration.exposeIdsFor(City.class);
        return args -> {
            cityRepository.save(new City("cityTest_01", 10, 2, 43));
            cityRepository.save(new City("cityTest_02", 20, 4, 83));
        };
    }
}

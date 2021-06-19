package ang.neggaw.movies;

import ang.neggaw.movies.entities.Category;
import ang.neggaw.movies.entities.Movie;
import ang.neggaw.movies.entities.MovieProjection;
import ang.neggaw.movies.entities.MovieSession;
import ang.neggaw.movies.services.CategoryService;
import ang.neggaw.movies.services.MovieService;
import ang.neggaw.movies.services.ProjectionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

import java.util.Date;

@EnableFeignClients
@EnableEurekaClient
@SpringBootApplication
public class M12_MovieMicroserviceApp {
    public static void main(String[] args) {
        SpringApplication.run(M12_MovieMicroserviceApp.class, args);
    }

    @Bean
    CommandLineRunner start(CategoryService categoryService,
                            MovieService movieService,
                            ProjectionService projectionService,
                            RepositoryRestConfiguration restConfiguration){

        restConfiguration.exposeIdsFor(Category.class, Movie.class, MovieProjection.class, MovieSession.class);

        return args -> {
            categoryService.createCategory(new Category("suspense"));

            movieService.createMovie(new Movie("movieTitleTest_01", 90.0, "directorName_01", "all about this movie...", "photoMovie.png", 1));
            movieService.createMovie(new Movie("movieTitleTest_02", 90.0, "directorName_02", "all about this movie...", "photoMovie.png", 1));

            projectionService.createProjection(new MovieProjection(new Date(), 17, 1, 1));
            projectionService.createProjection(new MovieProjection(new Date(), 17, 2, 2));
        };
    }
}

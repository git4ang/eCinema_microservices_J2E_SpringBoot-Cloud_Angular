package ang.neggaw.movies.restControllers;

import ang.neggaw.movies.entities.Movie;
import ang.neggaw.movies.services.MovieService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Log4j2
@RequestMapping(value = "/api/movies")
@RequiredArgsConstructor
@RestController
public class MovieRestController {
    
    private final MovieService movieService;

    // *********************************** Create a Movie ************************************** //
    @PostMapping
    public ResponseEntity<?> createMovie(@Valid @RequestBody Movie movie) {

        log.info("Creating Movie with title: '{}'", movie.getTitle());

        Object movieDB = movieService.createMovie(movie);
        if(movieDB.getClass().getSimpleName().equals("String")) {
            log.error(movieDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(movieDB);
        }

        log.info("Movie with id: '{}' CREATED successfully.", movie.getIdMovie());
        return ResponseEntity.status(HttpStatus.CREATED).body(movieDB);
    }


    // *********************************** Retrieve a Movie ************************************ //
    @GetMapping("/{idMovie}")
    public ResponseEntity<Object> getMovie(@PathVariable(value = "idMovie") long idMovie) {

        log.info("Fetching Movie with id: '{}'.", idMovie);

        Movie movie = movieService.getMovie(idMovie);
        if (movie == null) {
            String error = String.format("Unable to find Movie with id: '%s'.", idMovie);
            log.error(error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok(movie);
    }

    // *********************************** Retrieve all Movies ********************************* //
    @GetMapping
    public ResponseEntity<Collection<Movie>> allMovies(){

        log.info("Fetching all Movies of Cinema...");

        Collection<Movie> movies = movieService.allMovies();
        if(movies.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(movies);
    }

    // *********************************** Update a Movie ************************************** //
    @PutMapping("/{idMovie}")
    public ResponseEntity<?> updateMovie(@PathVariable(value = "idMovie") long idMovie,
                                           @Valid @RequestBody Movie movie) {

        log.info("Updating Movie with title: '{}'.", movie.getTitle());

        movie.setIdMovie(idMovie);
        Object movieDB = movieService.updateMovie(movie);
        if(movieDB.getClass().getSimpleName().equals("String")) {
            log.error(movieDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(movieDB);
        }

        log.info("Movie with id: {} UPDATED successfully.", idMovie);
        return ResponseEntity.ok(movieDB);
    }

    // *********************************** Delete a Movie ************************************** //
    @DeleteMapping("/{idMovie}")
    public ResponseEntity<?> deleteMovie(@PathVariable(value = "idMovie") long idMovie) {

        log.info("Deleting Movie with id: '{}'.", idMovie);

        Object movieDB = movieService.deleteMovie(idMovie);
        if(movieDB.getClass().getSimpleName().equals("String")) {
            log.error(movieDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(movieDB);
        }

        String movieDeleted = String.format("Movie with id: '%s' DELETED successfully.", idMovie);
        log.info(movieDeleted);
        return ResponseEntity.accepted().body(movieDeleted);
    }
}
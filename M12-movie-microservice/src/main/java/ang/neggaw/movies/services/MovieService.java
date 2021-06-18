package ang.neggaw.movies.services;

import ang.neggaw.movies.entities.Movie;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

@RepositoryRestResource
public interface MovieService {
    Object createMovie(Movie movie);
    Movie getMovie(long idMovie);
    Collection<Movie> allMovies();
    Object updateMovie(Movie movie);
    Object deleteMovie(long idMovie);
}

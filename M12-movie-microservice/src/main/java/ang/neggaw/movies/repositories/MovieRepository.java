package ang.neggaw.movies.repositories;

import ang.neggaw.movies.entities.Movie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface MovieRepository extends JpaRepository<Movie, Long> {
    Movie findByIdMovie(long idMovie);
    Movie findByTitle(String title);
}

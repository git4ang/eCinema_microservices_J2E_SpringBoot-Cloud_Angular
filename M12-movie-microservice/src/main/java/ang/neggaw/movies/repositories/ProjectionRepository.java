package ang.neggaw.movies.repositories;

import ang.neggaw.movies.entities.MovieProjection;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;

@RepositoryRestResource
public interface ProjectionRepository extends JpaRepository<MovieProjection, Long> {
    MovieProjection findByIdProjection(long idProjection);
    MovieProjection findByDateProjectionAndIdMovie(Date dateProj, long idMovie);
}

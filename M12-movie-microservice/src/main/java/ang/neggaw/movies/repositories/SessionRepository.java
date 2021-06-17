package ang.neggaw.movies.repositories;

import ang.neggaw.movies.entities.MovieSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Date;

@RepositoryRestResource
public interface SessionRepository extends JpaRepository<MovieSession, Long> {
    MovieSession findByIdSession(long idSession);
    MovieSession findByMovieStartTimeAndIdProjection(Date movieStartTime, long idProj);
}

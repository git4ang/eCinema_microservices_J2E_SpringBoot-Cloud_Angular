package ang.neggaw.cinemas.repositories;

import ang.neggaw.cinemas.entities.Cinema;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CinemaRepository extends JpaRepository<Cinema, Long> {
    Cinema findByIdCinema(long idCinema);
    Cinema findByName(String name);
    List<Cinema> findByIdCity(long idCity);
}

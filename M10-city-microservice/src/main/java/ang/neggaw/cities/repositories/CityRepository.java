package ang.neggaw.cities.repositories;

import ang.neggaw.cities.entities.City;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CityRepository extends JpaRepository<City, Long> {
    City findByIdCity(long idCity);
    City findByName(String name);
}

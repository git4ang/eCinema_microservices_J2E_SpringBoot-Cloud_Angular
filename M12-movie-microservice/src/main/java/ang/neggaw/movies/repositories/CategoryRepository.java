package ang.neggaw.movies.repositories;

import ang.neggaw.movies.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface CategoryRepository extends JpaRepository<Category, Long> {
    Category findByIdCategory(long idCat);
    Category findByName(String name);
}

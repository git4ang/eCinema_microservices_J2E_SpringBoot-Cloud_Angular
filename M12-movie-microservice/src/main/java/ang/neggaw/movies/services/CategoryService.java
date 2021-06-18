package ang.neggaw.movies.services;

import ang.neggaw.movies.entities.Category;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

@RepositoryRestResource
public interface CategoryService {
    Object createCategory(Category c);
    Category getCategory(long idCat);
    Collection<Category> allCategories();
    Object updateCategory(Category c);
    Object deleteCategory(long idCat);
}

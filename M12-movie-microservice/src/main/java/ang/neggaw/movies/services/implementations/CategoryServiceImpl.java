package ang.neggaw.movies.services.implementations;

import ang.neggaw.movies.entities.Category;
import ang.neggaw.movies.repositories.CategoryRepository;
import ang.neggaw.movies.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Log4j2
@RequiredArgsConstructor
@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    @Override
    public Object createCategory(Category c) {

        Category categoryDB = categoryRepository.findByName(c.getName());
        if (categoryDB != null) return String.format("Category already exists with this name: '%s'.", c.getName());

        c.setEntityState(Category.EntityState.CREATED);
        return categoryRepository.save(c);
    }

    @Override
    public Category getCategory(long idCat) { return categoryRepository.findByIdCategory(idCat); }

    @Override
    public Collection<Category> allCategories() { return categoryRepository.findAll(); }

    @Override
    public Object updateCategory(Category c) {

        Category categoryDB = categoryRepository.findByIdCategory(c.getIdCategory());
        if (categoryDB == null) return String.format("Unable to update. Category with id: '%s' Not Found.", c.getIdCategory());

        c.setEntityState(Category.EntityState.UPDATED);
        return categoryRepository.saveAndFlush(c);
    }

    @Override
    public Object deleteCategory(long idCat) {

        Category category = (Category) getCategory(idCat);
        if (category == null ) return String.format("Unable to delete. Category with id: '%s' Not Found.", idCat);

        categoryRepository.delete(category);
        category.setEntityState(Category.EntityState.DELETED);

        return category;
    }
}

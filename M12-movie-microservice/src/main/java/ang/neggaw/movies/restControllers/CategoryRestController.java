package ang.neggaw.movies.restControllers;

import ang.neggaw.movies.entities.Category;
import ang.neggaw.movies.services.CategoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Log4j2
@RequestMapping(value = "/api/categories")
@RequiredArgsConstructor
@RestController
public class CategoryRestController {

    private final CategoryService categoryService;


    // *********************************** Create a Category ************************************** //
    @PostMapping
    public ResponseEntity<?> createCategory(@Valid @RequestBody Category category) {

        log.info("Creating Category with name: '{}'.", category.getName());

        Object categoryDB = categoryService.createCategory(category);
        if (categoryDB.getClass().getSimpleName().equals("String")) {
            log.error(categoryDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(categoryDB);
        }

        log.info("Category with id: {} CREATED successfully.", category.getIdCategory());
        return ResponseEntity.status(HttpStatus.CREATED).body(categoryDB);
    }


    // *********************************** Retrieve a Category ************************************ //
    @GetMapping("/{idCat}")
    public ResponseEntity<Object> getCategory(@PathVariable(value = "idCat") long idCat) {

        log.info("Fetching a Category with id: '{}'.", idCat);

        Category categoryDB = categoryService.getCategory(idCat);
        if(categoryDB == null) {
            String error = String.format("Category not found with this id: '%s'.", idCat);
            log.error(error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        return ResponseEntity.ok(categoryDB);
    }


    // *********************************** Retrieve all Categories ******************************** //
    @GetMapping
    public ResponseEntity<Collection<Category>> allCategories() {

        log.info("Fetching all Categories of Movie...");

        try {
            return ResponseEntity.ok(categoryService.allCategories());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }


    // *********************************** Update a Category ************************************** //
    @PutMapping("/{idCat}")
    public ResponseEntity<?> updateCategory(@PathVariable(value = "idCat") long idCat,
                                            @Valid @RequestBody Category category) {

        log.info("Updating Category with id: '{}'.", category.getIdCategory());

        category.setIdCategory(idCat);
        Object categoryDB = categoryService.updateCategory(category);
        if (categoryDB.getClass().getSimpleName().equals("String")) {
            log.error(categoryDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(categoryDB);
        }

        log.info("Category with id: '{}' UPDATED successfully.", idCat);
        return ResponseEntity.ok(categoryDB);
    }


    // *********************************** Delete a Category ************************************** //
    @DeleteMapping("/{idCat}")
    public ResponseEntity<?> deleteCategory(@PathVariable(value = "idCat") Long idCat) {

        log.info("Deleting Category with id: '{}'", idCat);

        Object categoryDB = categoryService.deleteCategory(idCat);
        if (categoryDB.getClass().getSimpleName().equals("String")) {
            log.error(categoryDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(categoryDB);
        }

        String categoryDeleted = String.format("Category with id: '%s' DELETED successfully.", idCat);
        log.info(categoryDeleted);
        return ResponseEntity.accepted().body(categoryDeleted);
    }
}
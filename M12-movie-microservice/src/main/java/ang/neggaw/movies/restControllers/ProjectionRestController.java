package ang.neggaw.movies.restControllers;

import ang.neggaw.movies.entities.MovieProjection;
import ang.neggaw.movies.services.ProjectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Log4j2
@RequestMapping(value = "/api/projections")
@RequiredArgsConstructor
@RestController
public class ProjectionRestController {
    
    private final ProjectionService projectionService;

    // *********************************** Create a Projection ************************************** //
    @PostMapping
    public ResponseEntity<?> createProjection(@Valid @RequestBody MovieProjection projection) {

        log.info("Creating Projection for the Movie with id: '{}'", projection.getIdMovie());

        Object projectionDB = projectionService.createProjection(projection);
        if (projectionDB.getClass().getSimpleName().equals("String")){
            log.error(projectionDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(projectionDB);
        }

        log.info("Projection with id: {} CREATED successfully.", projection.getIdProjection());
        return ResponseEntity.status(HttpStatus.CREATED).body(projectionDB);
    }


    // *********************************** Retrieve a Projection ************************************ //
    @GetMapping("/{idProj}")
    public ResponseEntity<Object> getProjection(@PathVariable(value = "idProj") long idProj) {

        log.info("Fetching Projection with id: '{}'", idProj);

        Object projectionDB = projectionService.getProjection(idProj, false);
        if(projectionDB.getClass().getSimpleName().equals("String")) {
            log.error(projectionDB);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(projectionDB);
        }
        return ResponseEntity.ok(projectionDB);
    }

    @GetMapping("/{idProj}/{isFullProjection}")
    public ResponseEntity<Object> getProjection(@PathVariable(value = "idProj") long idProj,
                                                    @PathVariable(value = "isFullProjection") boolean isFullProjection) {

        log.info("Fetching full Projection with id: '{}'", idProj);

        Object projectionDB = projectionService.getProjection(idProj, isFullProjection);
        if(projectionDB.getClass().getSimpleName().equals("String")) {
            log.error(projectionDB);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(projectionDB);
        }
        return ResponseEntity.ok(projectionDB);
    }

    // *********************************** Retrieve all Projections ********************************* //
    @GetMapping
    public ResponseEntity<Collection<MovieProjection>> allProjections() {

        log.info("Fetching all Projection cinema...");

        try {
            return ResponseEntity.ok(projectionService.allProjections());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    // *********************************** Update a Projection ************************************** //
    @PutMapping("/{idProj}")
    public ResponseEntity<?> updateProjection(@PathVariable(value = "idProj") long idProj,
                                              @Valid @RequestBody MovieProjection projection) {

        log.info("Updating Projection with Film with id: '{}'.", projection.getIdMovie());

        projection.setIdProjection(idProj);
        Object projectionDB = projectionService.updateProjection(projection);
        if(projectionDB.getClass().getSimpleName().equals("String")) {
            log.error(projectionDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(projectionDB);
        }

        log.info("Projection with id: '{}' UPDATED successfully.", idProj);
        return ResponseEntity.ok(projectionDB);
    }


    // *********************************** Add Projection to Room *********************************** //
    @PutMapping(value = "/rooms/{idProj}/{idRoom}")
    public Object addProjectionToRoom(@PathVariable(value = "idProj") long idProj,
                                      @PathVariable(value = "idRoom") long idRoom) {

        log.info("Adding Projection with id: '{}' to Room with id: '{}'.", idProj, idRoom);

        Object projectionDB = projectionService.addProjectionToRoom(idProj, idRoom);
        if(projectionDB.getClass().getSimpleName().equals("String")) {
            log.error(projectionDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(projectionDB);
        }

        log.info("Projection with id: '{}' ADDED successfully to Room with id: '{}'.", idProj, idRoom);
        return ResponseEntity.ok(projectionDB);
    }



    // *********************************** Delete a Projection ************************************** //
    @DeleteMapping("/{idProj}")
    public ResponseEntity<?> deleteProjection(@PathVariable(value = "idProj") long idProj) {

        log.info("Deleting Projection with id: '{}'.", idProj);

        Object projectionDB = projectionService.deleteProjection(idProj);
        if(projectionDB.getClass().getSimpleName().equals("String")) {
            log.error(projectionDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(projectionDB);
        }

        String projectionDeleted = String.format("Projection with id: '%s' DELETED successfully", idProj);
        log.info(projectionDeleted);
        return ResponseEntity.accepted().body(projectionDeleted);
    }
}
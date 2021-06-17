package ang.neggaw.cinemas.restControllers;

import ang.neggaw.cinemas.entities.Cinema;
import ang.neggaw.cinemas.services.CinemaService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Log4j2
@RequiredArgsConstructor
@RequestMapping("/api/cinemas")
@RestController
public class CinemaRestController {

    private final CinemaService cinemaService;

    // **************************** Create a Cinema ************************************* //
    @PostMapping
    public ResponseEntity<?> createCinema(@Valid @RequestBody Cinema c) {

        log.info("Creating Cinema with name: '{}'...", c.getName());

        Object cinemaDB = cinemaService.createCinema(c);
        if (cinemaDB.getClass().getSimpleName().equals("String")) {
            log.error(cinemaDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cinemaDB);
        }

        log.info("Cinema with id: '{}' CREATED successfully.", c.getIdCinema());
        return ResponseEntity.status(HttpStatus.CREATED).body(cinemaDB);
    }

    // **************************** Retrieve a Cinema *********************************** //
    @GetMapping("/{idCinema}")
    public ResponseEntity<?> getCinema(@PathVariable(value = "idCinema") long idCinema) {

        log.info("Fetching Cinema with id: '{}'", idCinema);

        Cinema cinema = cinemaService.getCinema(idCinema, false);
        if (cinema == null) {
            String error = String.format("Unable to find Cinema with id: '%s'.", idCinema);
            log.error(error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok(cinema);
    }

    @GetMapping("/{idCinema}/{isFullCinema}")
    public ResponseEntity<?> getCinema(@PathVariable(value = "idCinema") long idCinema,
                                       @PathVariable(value = "isFullCinema") boolean isFullCinema) {

        log.info("Fetching full Cinema with id: '{}'.", idCinema);

        Cinema cinema = cinemaService.getCinema(idCinema, isFullCinema);
        if (cinema == null) {
            String error = String.format("Unable to find Cinema with id: '%s'.", idCinema);
            log.error(error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok(cinema);
    }

    // **************************** Retrieve all villes ********************************* //
    @GetMapping
    public ResponseEntity<Collection<Cinema>> allCinemas(){

        log.info("Fetching all Cinemas ...");

        Collection<Cinema> cinemas = cinemaService.allCinemas();
        if (cinemas.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).body(null);

        return ResponseEntity.ok(cinemas);
    }

    // **************************** Update a Cinema ************************************* //
    @PutMapping("/{idCinema}")
    public ResponseEntity<?> updateCinema(@PathVariable(value = "idCinema") long idCinema,
                                          @Valid @RequestBody Cinema c ){

        log.info("Updating Cinema with id: '{}'.", idCinema);

        c.setIdCinema(idCinema);
        Object cinemaDB = cinemaService.updateCinema(c);
        if(cinemaDB.getClass().getSimpleName().equals("String")) {
            log.error(cinemaDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cinemaDB);
        }

        log.info("Cinema with id: '{}' UPDATED successfully.", idCinema);
        return ResponseEntity.ok(cinemaDB);
    }

    // **************************** Update Cinema to other City ********************************* //
    @PutMapping("/{idCinema}/{idCityNew}/{idCityOld}")
    public ResponseEntity<?> updateCinemaToOtherCity(@PathVariable(value = "idCinema") long idCinema,
                                                     @PathVariable(value = "idCityNew") long idCityNew,
                                                     @PathVariable(value = "idCityOld") long idCityOld) {

        Object cinemaDB = cinemaService.updateCinemaToOtherCity(idCinema, idCityNew, idCityOld);
        if(cinemaDB.getClass().getSimpleName().equals("String")) {
            log.error(cinemaDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cinemaDB);
        }

        return ResponseEntity.status(HttpStatus.OK).body(cinemaDB);
    }


    // **************************** Delete a Ville ************************************** //
    @DeleteMapping("/{idCinema}")
    public ResponseEntity<?> deleteCinema(@PathVariable(value = "idCinema") long idCinema) {

        log.info("Deleting Cinema with id: '{}'.", idCinema);

        Object cinema = cinemaService.deleteCinema(idCinema);
        if (cinema.getClass().getSimpleName().equals("String")) {
            log.error(cinema);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cinema);
        }

        String cinemaDeleted = String.format("Cinema with id: '%s' DELETED successfully.", idCinema);
        log.info(cinemaDeleted);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(cinemaDeleted);
    }
}

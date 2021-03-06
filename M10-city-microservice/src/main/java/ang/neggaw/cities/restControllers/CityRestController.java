package ang.neggaw.cities.restControllers;

import ang.neggaw.cities.entities.City;
import ang.neggaw.cities.services.CityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Log4j2
@RequestMapping(value = "/api/cities")
@RequiredArgsConstructor
@RestController
public class CityRestController {

    private final CityService cityService;

    // ****************************** Create a City ******************************* //
    @PostMapping
    public ResponseEntity<?> createCity(@Valid @RequestBody City c) {

        log.info("Creating City with name: '{}'...", c.getName());

        Object cityDB = cityService.createCity(c);
        if (cityDB.getClass().getSimpleName().equals("String")) {
            log.error(cityDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cityDB);
        }

        log.info("City with id: {} CREATED successfully.", c.getIdCity());
        return new ResponseEntity<>(cityDB, HttpStatus.CREATED);
    }

    // **************************** Retrieve a single city ***************************** //
    @GetMapping("/{idCity}")
    public ResponseEntity<Object> getCity(@PathVariable(value = "idCity") long idCity) {

        log.info("Fetching City with id: {}", idCity);

        City city = cityService.getCity(idCity, false);
        if(city == null) {
            String error = String.format("Unable to find City with id: '%s'", idCity);
            log.error(error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok(city);
    }

    @GetMapping("/{idCity}/{isFullCity}")
    public ResponseEntity<?> getCity(@PathVariable(value = "idCity") long idCity,
                                     @PathVariable(value = "isFullCity") boolean isFullCity) {

        log.info("Fetching full City with id: {}", idCity);

        City city = cityService.getCity(idCity, isFullCity);
        if(city == null) {
            String error = String.format("Unable to find City with id: '%s'", idCity);
            log.error(error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok(city);
    }


    // **************************** Retrieve all citys ********************************* //
    @GetMapping
    public ResponseEntity<Collection<City>> allCities() {

        log.info("Fetching all Cities...");

        try {
            return ResponseEntity.ok(cityService.allCities());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    // **************************** Update a City ************************************** //
    @PutMapping("/{idCity}")
    public ResponseEntity<?> updateCity(@PathVariable(value = "idCity") long idCity,
                                        @Valid @RequestBody City c) {

        log.info("Updating City with id: {}", idCity);

        c.setIdCity(idCity);
        Object cityDB = cityService.updateCity(c);
        if (cityDB.getClass().getSimpleName().equals("String")) {
            log.error(cityDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cityDB);
        }

        log.info("City with id: {} UPDATED successfully.", idCity);
        return ResponseEntity.ok(cityDB);
    }

    // **************************** Delete a City ************************************** //
    @DeleteMapping(value = "/{idCity}")
    public ResponseEntity<?> deleteCity(@PathVariable(value = "idCity") long idCity) {

        log.info("Deleting City with id: {}", idCity);

        Object cityDB = cityService.deleteCity(idCity);
        if (cityDB.getClass().getSimpleName().equals("String")) {
            log.error(cityDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(cityDB);
        }

        String cityDeleted = String.format("City with id: '%s' DELETED successfully.", idCity);
        log.info(cityDeleted);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(cityDeleted);
    }
}

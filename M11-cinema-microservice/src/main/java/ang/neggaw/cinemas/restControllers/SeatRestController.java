package ang.neggaw.cinemas.restControllers;

import ang.neggaw.cinemas.entities.Seat;
import ang.neggaw.cinemas.services.SeatService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Log4j2
@AllArgsConstructor
@RequestMapping("/api/seats")
@RestController
public class SeatRestController {

    private final SeatService seatService;


    // ********************************** Create a Seat ******************************** //
    @PostMapping
    public ResponseEntity<?> createSeat(@Valid @RequestBody Seat s) {

        log.info("Creating Seat with refSeat: '{}'.", s.getRefSeat());

        Object seatDB = seatService.createSeat(s);
        if (seatDB.getClass().getSimpleName().equals("String")) {
            log.error(seatDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(seatDB);
        }

        log.info("Seat with id: '{}' CREATED successfully", s.getIdSeat());
        return ResponseEntity.status(HttpStatus.CREATED).body(seatDB);
    }

    // ********************************** Get a Seat *********************************** //
    @GetMapping("/{id}")
    public ResponseEntity<?> getSeat(@PathVariable(value = "id") long idSeat) {

        log.info("Fetching Seat with id: '{}'.", idSeat);

        Seat seat = seatService.getSeat(idSeat);
        if(seat == null) {
            String error = String.format("Seat with id: '%s' Not Found", idSeat);
            log.error(error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok(seat);
    }

    // ********************************** All Seats ************************************ //
    @GetMapping
    public ResponseEntity<Collection<Seat>> allSeat() {

        log.info("Fetching all Seats cinema");

        Collection<Seat> seats = seatService.allSeats();
        if(seats.isEmpty()) return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        return ResponseEntity.ok().body(seats);
    }

    // ********************************** Update a Seat ******************************** //
    @PutMapping("/{idSeat}")
    public ResponseEntity<?> updateSeat(@PathVariable(value = "idSeat") long idSeat,
                                        @Valid @RequestBody Seat p) {

        log.info("Updating Seat with id: '{}'", idSeat);

        p.setIdSeat(idSeat);
        Object seatDB = seatService.updateSeat(p);
        if(seatDB.getClass().getSimpleName().equals("String")) {
            log.error(seatDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(seatDB);
        }

        log.info("Seat with id: '{}' UPDATED successfully", idSeat);
        return ResponseEntity.ok().body(seatDB);
    }


    // ********************************** Delete a Seat ******************************** //
    @DeleteMapping("/{idSeat}")
    public ResponseEntity<?> deleteSeat(@PathVariable(value = "idSeat") long idSeat) {

        log.info("Deleting Seat with id: '{}'", idSeat);

        Object seatDB = seatService.deleteSeat(idSeat);
        if(seatDB.getClass().getSimpleName().equals("String")) {
            log.error(seatDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(seatDB);
        }

        String seatDeleted = String.format("Seat with id: '%s' DELETED successfully", idSeat);
        log.info(seatDeleted);
        return ResponseEntity.accepted().body(seatDeleted);
    }
}

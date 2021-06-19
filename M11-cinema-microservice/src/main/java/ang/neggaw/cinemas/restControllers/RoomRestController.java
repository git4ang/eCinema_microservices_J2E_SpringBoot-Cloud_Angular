package ang.neggaw.cinemas.restControllers;

import ang.neggaw.cinemas.entities.Room;
import ang.neggaw.cinemas.services.RoomService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;
import java.util.List;

@Log4j2
@AllArgsConstructor
@RequestMapping("/api/rooms")
@RestController
public class RoomRestController {

    private final RoomService roomService;

    // ******************************** Create a Room ************************************** //
    @PostMapping
    public ResponseEntity<?> createRoom(@Valid @RequestBody Room r) {

        log.info("Creating Room with name {}", r.getName());

        Object room = roomService.createRoom(r);
        if(room.getClass().getSimpleName().equals("String")) {
            log.error(room);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(room);
        }

        log.info("Room with id: '{}' CREATED successfully.", r.getIdRoom());
        return ResponseEntity.status(HttpStatus.CREATED).body(room);
    }

    // ******************************** Get a Room ***************************************** //
    @GetMapping("/{idRoom}")
    public ResponseEntity<?> getRoom(@PathVariable(value = "idRoom") long idRoom) {

        log.info("Fetching Room with id: '{}'", idRoom);

        Room room = roomService.getRoom(idRoom, false);
        if (room == null) {
            String error = String.format("Unable to find Room entity with id: '%s'", idRoom);
            log.error(error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok().body(room);
    }

    @GetMapping("/{idRoom}/{isFullRoom}")
    public ResponseEntity<?> getRoom(@PathVariable(value = "idRoom") long idRoom,
                                     @PathVariable(value = "isFullRoom") boolean isFullRoom) {

        log.info("Fetching full Room with id: '{}'", idRoom);

        Room room = roomService.getRoom(idRoom, isFullRoom);
        if (room == null) {
            String error = String.format("Unable to find Room entity with id: '%s'", idRoom);
            log.error(error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }
        return ResponseEntity.ok().body(room);
    }

    // ******************************** All Rooms ****************************************** //
    @GetMapping
    public ResponseEntity<Collection<Room>> allRooms() {
        log.info("Fetching all Rooms cinema");

        try {
            return ResponseEntity.ok(roomService.allRooms());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
    }

    // ******************************** Update a Room ************************************** //
    @PutMapping("/{idRoom}")
    public ResponseEntity<?> updateRoom(@PathVariable(value = "idRoom") long idRoom,
                                        @Valid @RequestBody Room s) {

        log.info("Updating Room entity with id: '{}'", idRoom);

        s.setIdRoom(idRoom);
        Object roomDB = roomService.updateRoom(s);
        if(roomDB.getClass().getSimpleName().equals("String")) {
            log.error(roomDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(roomDB);
        }

        log.info("Room with id: '{}' UPDATED successfully", idRoom);
        return ResponseEntity.ok(roomDB);
    }


    // ******************************** Add Room to Cinema ********************************** //
    @PutMapping(value = {"{idRoom}/{idCinema}", "/cinemas/{idRoom}/{idCinema}"})
    public ResponseEntity<?> addRoomToCinema(@PathVariable(value = "idRoom") long idRoom,
                                           @PathVariable(value = "idCinema") long idCinema) {

        log.info("Adding Room with id: '{}' to Cinema with id: '{}'", idRoom, idCinema);

        Object roomDB = roomService.addRoomToCinema(idRoom, idCinema);
        if(roomDB.getClass().getSimpleName().equals("String")) {
            log.error(roomDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(roomDB);
        }

        return ResponseEntity.status(HttpStatus.OK).body(roomDB);
    }

    // ******************************** Add Seat to Room ********************************** //
    @PutMapping("/seats/{idSeat}/{idRoom}")
    public ResponseEntity<?> removeSeatFromRoom(@PathVariable(value = "idSeat") long idSeat,
                                                @PathVariable(value = "idRoom") long idRoom) {

        log.info("Removing Seat with id: '{}' from Room with id: '{}'", idSeat, idRoom);
        Object roomDB = roomService.removeSeatFromRoom(idSeat, idRoom);
        if(roomDB.getClass().getSimpleName().equals("String")) {
            log.error(roomDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(roomDB);
        }

        return ResponseEntity.status(HttpStatus.OK).body(roomDB);
    }

    // ******************************** Delete a Room ************************************** //
    @DeleteMapping("/{idRoom}")
    public ResponseEntity<?> deleteRoom(@PathVariable(value = "idRoom") long idRoom) {

        log.info("Deleting Room with id: {}", idRoom);

        Object roomDB = roomService.deleteRoom(idRoom);
        if(roomDB.getClass().getSimpleName().equals("String")) {
            log.error(roomDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        String roomDeleted = String.format("Room with id: '%s' DELETED successfully", idRoom);
        log.info(roomDeleted);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(roomDeleted);
    }
}

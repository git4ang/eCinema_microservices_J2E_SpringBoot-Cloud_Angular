package ang.neggaw.tickets.restControllers;

import ang.neggaw.tickets.entities.MovieTicket;
import ang.neggaw.tickets.services.TicketService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@RestController
@AllArgsConstructor
@Log4j2
@RequestMapping("/api/tickets")
public class TicketRestController {

    private final TicketService ticketService;


    // ********************************* Create a Ticket ******************************************* //
    @PostMapping
    public ResponseEntity<?> createTicket(@Valid @RequestBody MovieTicket t){

        log.info("Creating Ticket cinema ...");

        Object ticketDB = ticketService.createTicket(t);
        if(ticketDB.getClass().getSimpleName().equals("String")) {
            log.error(ticketDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ticketDB);
        }

        log.info("Ticket with id '{}' CREATED successfully.", t.getIdTicket());
        return ResponseEntity.status(HttpStatus.CREATED).body(ticketDB);
    }

    // ********************************* Retrieve a Ticket ***************************************** //
    @GetMapping("/{idTicket}")
    public ResponseEntity<Object> getTicket(@PathVariable(value = "idTicket") long idTicket) {

        log.info("Fetching Ticket with id: '{}'.", idTicket);

        MovieTicket ticket = ticketService.getTicket(idTicket);
        if(ticket == null) {
            String error = String.format("Unable to find Ticket with id: '%s'", idTicket);
            log.error(error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        return ResponseEntity.ok(ticket);
    }

    // ********************************* Retrieve all Tickets ************************************** //
    @GetMapping
    public ResponseEntity<Collection<MovieTicket>> allTickets() {

        log.info("Fetching all Tickets of Cinema ...");

        Collection<MovieTicket> tickets = ticketService.allTickets();
        if(tickets.isEmpty()) return ResponseEntity.noContent().build();

        return ResponseEntity.ok(tickets);
    }

    // ********************************* Update a Ticket ******************************************* //
    @PutMapping("/{idTicket}")
    public ResponseEntity<?> updateTicket(@PathVariable(value = "idTicket") long idTicket,
                                          @Valid @RequestBody MovieTicket t){

        log.info("Updating Ticket with id: {}", idTicket);

        t.setIdTicket(idTicket);
        Object ticketDB = ticketService.updateTicket(t);
        if(ticketDB.getClass().getSimpleName().equals("String")) {
            log.error(ticketDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ticketDB);
        }

        log.info("Ticket with id: '{}' UPDATED successfully.", t.getIdTicket());
        return ResponseEntity.ok(ticketDB);
    }

    // ********************************* Add Ticket to Projection ******************************************* //
    @PutMapping(value = {"/projections/{idTicket}/{idProj}"})
    public ResponseEntity<Object> addTicketToProjection(@PathVariable(value = "idTicket") long idTicket,
                                        @PathVariable(value = "idProj") long idProj) {

        Object ticketDB = ticketService.addTicketToProjection(idTicket, idProj);
        if(ticketDB.getClass().getSimpleName().equals("String")) {
            log.error(ticketDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ticketDB);
        }

        log.info("Ticket with id: '{}' ADDED successfully to Projection with id: '{}'.", idTicket, idProj);
        return ResponseEntity.status(HttpStatus.OK).body(ticketDB);
    }

    // ********************************* Add Ticket to Seat ******************************************* //
    @PutMapping(value = {"/seats/{idTicket}/{idSeat}"})
    public ResponseEntity<Object> addTicketToSeat(@PathVariable(value = "idTicket") long idTicket,
                                                  @PathVariable(value = "idSeat") long idSeat) {

        Object ticketDB = ticketService.addTicketToSeat(idTicket, idSeat);
        if(ticketDB.getClass().getSimpleName().equals("String")) {
            log.error(ticketDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ticketDB);
        }

        log.info("Ticket with id: '{}' ADDED successfully to Seat with id: '{}'.", idTicket, idSeat);
        return ResponseEntity.status(HttpStatus.OK).body(ticketDB);
    }

    // ********************************* Delete a Ticket ******************************************* //
    @DeleteMapping("/{idTicket}")
    public ResponseEntity<?> deleteTicket(@PathVariable(value = "idTicket") long idTicket) {

        log.info("Deleting Ticket with id: {}", idTicket);

        Object ticketDB = ticketService.deleteTicket(idTicket);
        if(ticketDB.getClass().getSimpleName().equals("String")) {
            log.error(ticketDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ticketDB);
        }

        String ticketDeleted = String.format("Ticket with id: '%s' DELETED successfully.", idTicket);
        log.info(ticketDeleted);
        return ResponseEntity.accepted().body(ticketDeleted);
    }
}

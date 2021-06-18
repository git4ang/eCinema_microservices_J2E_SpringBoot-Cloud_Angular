package ang.neggaw.cinemas.proxies;

import ang.neggaw.cinemas.beans.TicketProxy;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "ticket-microservice", url = "http://localhost:8400")
@RequestMapping(value = "/api/tickets")
public interface TicketRestProxy {

    @GetMapping("/{idTicket}")
    ResponseEntity<TicketProxy> getTicket(@PathVariable(value = "idTicket") long idTicket);

    @PutMapping("/{idTicket}")
    ResponseEntity<TicketProxy> updateTicket(@PathVariable(value = "idTicket") long idTicket,
                                             @RequestBody TicketProxy ticket);

    @DeleteMapping("/{idTicket}")
    ResponseEntity<TicketProxy> deleteTicket(@PathVariable(value = "idTicket") long idTicket);

}

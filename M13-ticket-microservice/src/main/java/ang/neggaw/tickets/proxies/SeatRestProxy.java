package ang.neggaw.tickets.proxies;

import ang.neggaw.tickets.beans.SeatProxy;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "cinema-microservice", url = "http://localhost:8200")
@RequestMapping("/api/seats")
public interface SeatRestProxy {

    @GetMapping("/{idSeat}")
    ResponseEntity<SeatProxy> getSeat(@PathVariable(value = "idSeat") long idSeat);

    @PutMapping("/{idSeat}")
    ResponseEntity<SeatProxy> updateSeat(@PathVariable(value = "idSeat") long idSeat,
                                         @RequestBody SeatProxy p);

    @DeleteMapping("/{idSeat}")
    ResponseEntity<SeatProxy> deleteSeat(@PathVariable(value = "idSeat") long idSeat);
}

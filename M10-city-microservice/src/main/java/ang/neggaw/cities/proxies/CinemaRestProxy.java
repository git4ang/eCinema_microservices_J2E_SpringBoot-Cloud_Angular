package ang.neggaw.cities.proxies;

import ang.neggaw.cities.beans.CinemaProxy;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "cinema-microservice", url = "http://localhost:8200")
@RequestMapping(value = "/api/cinemas")
public interface CinemaRestProxy {

    @GetMapping(value = "/{idCinema}")
    ResponseEntity<CinemaProxy> getCinema(@PathVariable(value = "idCinema") long idCinema);

    @GetMapping(value = "/{idCinema}/{isFullCinema}")
    ResponseEntity<CinemaProxy> getCinema(@PathVariable(value = "idCinema") long idCinema,
                                          @PathVariable(value = "isFullCinema") boolean isFullCinema);

    @PutMapping(value = "/{idCinema}")
    ResponseEntity<CinemaProxy> updateCinema(@PathVariable(value = "idCinema") long idCinema,
                                             @RequestBody CinemaProxy cinema );

    @DeleteMapping(value = "/{idCinema}")
    ResponseEntity<String> deleteCinema(@PathVariable(value = "idCinema") long idCinema);
}

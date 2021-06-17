package ang.neggaw.cinemas.proxies;

import ang.neggaw.cinemas.beans.CityProxy;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@FeignClient(name = "city-microservice", url = "http://localhost:8100")
@RequestMapping(value = "/api/cities")
public interface CityRestProxy {

    @GetMapping(value = "/{idCity}", produces = "application/json; charset=UTF-8")
    ResponseEntity<CityProxy> getCity(@PathVariable(value = "idCity") long idCity);

    @GetMapping("/{idCity}/{isFullCinema}")
    ResponseEntity<CityProxy> getCity(@PathVariable(value = "idCity") long idCity,
                              @PathVariable(value = "isFullCinema") boolean isFullCinema);

    @PutMapping("/{idCity}")
    ResponseEntity<CityProxy> updateCity(@PathVariable(value = "idCity") long idCity,
                                 @Valid @RequestBody CityProxy v);

    @DeleteMapping(value = "/{idCity}")
    ResponseEntity<CityProxy> deleteCity(@PathVariable(value = "idCity") long idCity);
}

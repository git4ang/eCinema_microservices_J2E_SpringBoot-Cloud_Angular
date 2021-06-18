package ang.neggaw.movies.proxies;

import ang.neggaw.movies.beans.RoomProxy;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "cinema-microservice", url = "http://localhost:8200")
@RequestMapping("/api/rooms")
public interface RoomRestProxy {

    @GetMapping("/{idRoom}")
    ResponseEntity<RoomProxy> getRoom(@PathVariable(value = "idRoom") long idRoom);

    @GetMapping("/{idRoom}/{isFullRoom}")
    ResponseEntity<RoomProxy> getRoom(@PathVariable(value = "idRoom") long idRoom,
                                      @PathVariable(value = "isFullRoom") boolean isFullRoom);

    @PutMapping("/{idRoom}")
    ResponseEntity<RoomProxy> updateRoom( @PathVariable(value = "idRoom") long idRoom,
                                          @RequestBody RoomProxy room);

    @DeleteMapping("/{idRoom}")
    ResponseEntity<RoomProxy> deleteRoom(@PathVariable(value = "idRoom") long idRoom);
}


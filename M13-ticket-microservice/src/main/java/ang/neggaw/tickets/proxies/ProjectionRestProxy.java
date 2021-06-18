package ang.neggaw.tickets.proxies;

import ang.neggaw.tickets.beans.ProjectionProxy;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "movie-microservice", url = "http://localhost:8300")
@RequestMapping("/api/projections")
public interface ProjectionRestProxy {

    @GetMapping("/{idProj}")
    ResponseEntity<ProjectionProxy> getProjection(@PathVariable(value = "idProj") long idProj);

    @GetMapping("/{idProj}/{isFullProjection}")
    ResponseEntity<ProjectionProxy> getProjection(@PathVariable(value = "idProj") long idProj,
                                                  @PathVariable(value = "isFullProjection") boolean isFullProjection);

    @PutMapping("/{idProj}")
    ResponseEntity<ProjectionProxy> updateProjection(@PathVariable(value = "idProj") long idProj,
                                                     @RequestBody ProjectionProxy projection);

    @DeleteMapping("/{idProj}")
    ResponseEntity<String> deleteProjection(@PathVariable(value = "idProj") long idProj);

}

package ang.neggaw.users.restControllers;

import ang.neggaw.users.entities.RoleReact;
import ang.neggaw.users.services.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.List;

/**
 * @author ANG
 * @since 12-07-2021 17:11
 */

@Log4j2
@RequestMapping(value = {"/api/roles" })
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class RoleRestController {

    private final RoleService roleService;

    @PostMapping
    public Mono<ResponseEntity<RoleReact>> createRole(@Valid @RequestBody RoleReact roleReact) {
        return roleService.createRole(roleReact)
                .map(r -> {
                    log.info("Role with id: '{}' CREATED successfully.", r.getIdRole());
                    return ResponseEntity.status(HttpStatus.CREATED).body(r);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @GetMapping(value = "/{idRole}")
    public Mono<ResponseEntity<RoleReact>> getRoleById(@PathVariable(value = "idRole") String idRole) {
        return roleService.getRoleById(idRole)
                .map(roleDB -> ResponseEntity.status(HttpStatus.OK).body(roleDB))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping(value = "/names/{name}")
    public Mono<ResponseEntity<RoleReact>> getRoleByName(@PathVariable(value = "name") String name) {
        return roleService.getRoleByRoleName(name)
                .map(roleDB -> ResponseEntity.status(HttpStatus.OK).body(roleDB))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public Flux<ResponseEntity<RoleReact>> allRoles() {
        return roleService.allRoles()
                .map(roles -> ResponseEntity.status(HttpStatus.OK).body(roles))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @GetMapping(value = "/multiRoles/ids")
    public Flux<ResponseEntity<RoleReact>> multiRolesByIds(List<String> rolesIds) {
        return roleService.allRolesByIds(rolesIds)
                .map(roles -> ResponseEntity.status(HttpStatus.OK).body(roles))
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }

    @PutMapping(value = "/{idRole}")
    public Mono<ResponseEntity<RoleReact>> updateRoleById(@PathVariable(value = "idRole") String idRole,
                                                          @Valid @RequestBody RoleReact roleReact) {
        return roleService.updateRole(idRole, roleReact)
                .map(roleDB -> {
                    log.info("Role with id: '{}' UPDATED successfully.", roleDB.getIdRole());
                    return ResponseEntity.status(HttpStatus.OK).body(roleDB);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());

    }

    @DeleteMapping(value = "/{idRole}")
    public Mono<ResponseEntity<RoleReact>> deleteRoleById(@PathVariable(value = "idRole") String idRole) {

        return roleService.deleteRoleById(idRole)
                .map(roleDB -> {
                    log.info("Role with id: '{}' DELETED successfully.", idRole);
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(roleDB);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    @DeleteMapping(value = "/names/{roleName}")
    public Mono<ResponseEntity<RoleReact>> deleteRoleByRoleName(@PathVariable(value = "roleName") String roleName) {

        return roleService.deleteRoleByRoleName(roleName)
                .map(roleDB -> {
                    log.info("Role with roleName: '{}' DELETED successfully.", roleName);
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(roleDB);
                })
                .defaultIfEmpty(ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}

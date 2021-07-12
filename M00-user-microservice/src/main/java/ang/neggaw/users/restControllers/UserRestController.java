package ang.neggaw.users.restControllers;

import ang.neggaw.users.entities.UserReact;
import ang.neggaw.users.services.UserService;
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
 * @since 12-07-2021 17:12
 */

@Log4j2
@RequestMapping(value = {"/api/users" })
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RestController
public class UserRestController {

    private final UserService userService;


    @PostMapping
    Mono<Object> createUser(@Valid @RequestBody UserReact userReact) {
        return userService.createUser(userReact)
                .map(u -> {
                    log.info("User with id: '{}' CREATED successfully.", u.getIdUser());
                    return ResponseEntity.status(HttpStatus.CREATED).body(u);
                })
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
                });

    }

    @GetMapping(value = "/{idUser}")
    Mono<Object> getUserById(@PathVariable(value = "idUser") String idUser) {
        return userService.getUserById(idUser)
                .map(userDB -> ResponseEntity.status(HttpStatus.OK).body(userDB))
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()));
                });
    }

    @GetMapping(value = "/names/{name}")
    Mono<Object> getUserByUsername(@PathVariable(value = "name")String username) {
        return userService.getUserByUsername(username)
                .map(userDB -> ResponseEntity.status(HttpStatus.OK).body(userDB))
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()));
                });
    }

    @GetMapping(value = "/emails/{email}")
    Mono<Object> getUserByEmail(@PathVariable(value = "email") String email) {
        return userService.getUserByEmail(email)
                .map(userDB -> ResponseEntity.status(HttpStatus.OK).body(userDB))
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage()));
                });
    }

    @GetMapping
    Flux<Object> allUsers() {
        return userService.allUsers()
                .map(usersDB -> ResponseEntity.status(HttpStatus.OK).body(usersDB))
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage()));
                });
    }

    @GetMapping(value = "/multiUsers/ids")
    Flux<Object> allUsersByIds(List<String> usersIds) {
        return userService.allUsersByIds(usersIds)
                .map(usersDB -> ResponseEntity.status(HttpStatus.OK).body(usersDB))
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.NO_CONTENT).body(e.getMessage()));
                });
    }

    @PutMapping(value = "/{idUser}")
    Mono<Object> updateUser(@PathVariable(value = "idUser") String idUser,
                            @Valid @RequestBody UserReact userReact) {
        return userService.getUserById(idUser)
                .map(userDB -> {
                    log.info("User with id: '{}' UPDATED successfully.", userDB.getIdUser());
                    return ResponseEntity.status(HttpStatus.OK).body(userDB);
                })
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
                });
    }

    @PutMapping(value = "/{idUser}/{idRole}/{isRemoved}")
    public Mono<Object> addOrRemoveUserToRole(@PathVariable(value = "idUser") String idUser,
                                              @PathVariable(value = "idRole") String idRole,
                                              @PathVariable(value = "isRemoved", required = false) boolean isRemoved) {

        return userService.addOrRemoveUserToRole(idUser, idRole, isRemoved)
                .map(userReact -> {
                    log.info("User with id: '{}' ADDED/REMOVED successfully to Role with id: '{}'.", idUser, idRole);
                    return ResponseEntity.status(HttpStatus.OK).body(userReact);
                })
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
                });
    }

    @DeleteMapping(value = "/{idUser}")
    Mono<Object> deleteUserById(@PathVariable(value = "idUser") String idUser) {

        return userService.deleteUserById(idUser)
                .map(userDB -> {
                    log.info("User with id: '{}' DELETED successfully.", userDB.getIdUser());
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(userDB);
                })
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
                });
    }

    @DeleteMapping(value = "/names/{username}")
    Mono<Object> deleteUserByUsername(@PathVariable(value = "username") String username) {

        return userService.deleteUserByUsername(username)
                .map(userDB -> {
                    log.info("User with username: '{}' DELETED successfully.", userDB.getUsername());
                    return ResponseEntity.status(HttpStatus.ACCEPTED).body(userDB);
                })
                .cast(Object.class)
                .onErrorResume(e -> {
                    log.error(e.getMessage());
                    return Mono.just(ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage()));
                });
    }
}

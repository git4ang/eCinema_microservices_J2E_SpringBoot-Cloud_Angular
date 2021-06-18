package ang.neggaw.movies.restControllers;

import ang.neggaw.movies.entities.MovieSession;
import ang.neggaw.movies.services.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Collection;

@Log4j2
@RequestMapping(value = "/api/sessions")
@RequiredArgsConstructor
@RestController
public class SessionRestController {

    private final SessionService sessionService;

    // *********************************** Create a Session ************************************** //
    @PostMapping
    public ResponseEntity<?> createMovieSession(@Valid @RequestBody MovieSession session) {

        log.info("Creating Session with Projection with id: '{}'.", session.getIdProjection());

        Object sessionDB = sessionService.createSession(session);
        if (sessionDB.getClass().getSimpleName().equals("String")) {
            log.error(sessionDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sessionDB);
        }

        log.info("Session with id: '{}' CREATED successfully.", session.getIdSession());
        return ResponseEntity.status(HttpStatus.CREATED).body(sessionDB);
    }


    // *********************************** Retrieve a Session ************************************ //
    @GetMapping("/{idSession}")
    public ResponseEntity<Object> getSession(@PathVariable(value = "idSession") long idSession) {

        log.info("Fetching Session with id: '{}'.", idSession);

        MovieSession session = sessionService.getSession(idSession);
        if(session == null) {
            String error = String.format("Unable to find Session with id: '%s'.", idSession);
            log.error(error);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        }

        return ResponseEntity.ok(session);
    }

    // *********************************** Retrieve all MovieSessions ********************************* //
    @GetMapping
    public ResponseEntity<Collection<MovieSession>> allMovieSessions() {

        log.info("Fetching all Session of Projection...");

        Collection<MovieSession> sessions = sessionService.allSessions();
        if(sessions.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(sessions);
    }

    // *********************************** Update a Session ************************************** //
    @PutMapping("/{idSession}")
    public ResponseEntity<?> updateCategory(@PathVariable(value = "idSession") long idSession,
                                            @Valid @RequestBody MovieSession session) {

        log.info("Updating Session with id of projectionFilm: '{}'.", session.getIdProjection());

        session.setIdSession(idSession);
        Object sessionDB = sessionService.updateSession(session);
        if(sessionDB.getClass().getSimpleName().equals("String")) {
            log.error(sessionDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sessionDB);
        }

        log.info("Session with id: '{}' UPDATED successfully.", session.getIdSession());
        return ResponseEntity.ok(sessionDB);
    }

    // *********************************** Delete a Session ************************************** //
    @DeleteMapping("/{idSession}")
    public ResponseEntity<?> deleteMovieSession(@PathVariable(value = "idSession") long idSession) {

        log.info("Deleting Session with id: '{}'.", idSession);

        Object sessionDB = sessionService.deleteSession(idSession);
        if(sessionDB.getClass().getSimpleName().equals("String")) {
            log.error(sessionDB);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sessionDB);
        }

        String sessionDeleted = String.format("Session with id '%s' DELETED successfully.", idSession);
        log.info(sessionDeleted);
        return ResponseEntity.accepted().body(sessionDeleted);
    }
}
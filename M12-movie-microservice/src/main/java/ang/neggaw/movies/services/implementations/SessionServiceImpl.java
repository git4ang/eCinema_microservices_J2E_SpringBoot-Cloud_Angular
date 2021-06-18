package ang.neggaw.movies.services.implementations;

import ang.neggaw.movies.entities.MovieProjection;
import ang.neggaw.movies.entities.MovieSession;
import ang.neggaw.movies.repositories.ProjectionRepository;
import ang.neggaw.movies.repositories.SessionRepository;
import ang.neggaw.movies.services.SessionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;


@Log4j2
@RequiredArgsConstructor
@Service
public class SessionServiceImpl implements SessionService {
    
    private final SessionRepository sessionRepository;
    private final ProjectionRepository projectionRepository;



    @Override
    public Object createSession(MovieSession s) {

        MovieProjection projection = projectionRepository.findByIdProjection(s.getIdProjection());
        if (projection == null) return String.format("Session with Projection with id: '%s' Not Found.", s.getIdProjection());

        MovieSession sessionDB = sessionRepository.findByMovieStartTimeAndIdProjection(s.getMovieStartTime(), projection.getIdProjection());
        if (sessionDB != null) return String.format("Session already exists with id Projection '%s'.", s.getIdProjection());

        s.setEntityState(MovieSession.EntityState.CREATED);
        s.setProjection(projection);
        s.setMovieStartTime(new Date());
        return sessionRepository.save(s);
    }

    @Override
    public MovieSession getSession(long idSession) { return sessionRepository.findByIdSession(idSession); }

    @Override
    public Collection<MovieSession> allSessions() { return sessionRepository.findAll(); }

    @Override
    public Object updateSession(MovieSession s) {

        MovieSession sessionDB = getSession(s.getIdSession());
        if(sessionDB == null) return String.format("Unable to update. Session with id: '%s' Not Found.", s.getIdSession());

        MovieProjection projection = projectionRepository.findByIdProjection(s.getIdProjection());
        if(projection == null) return String.format("Unable to update. Session with id '%s' with projection with id '%s' Not Found.", s.getIdSession(), s.getIdProjection());
        s.setProjection(projection);

        s.setEntityState(MovieSession.EntityState.UPDATED);
        return sessionRepository.saveAndFlush(s);
    }

    @Override
    public Object deleteSession(long idSession) {

        MovieSession sessionDB = getSession(idSession);
        if(sessionDB == null) return String.format("Unable to delete. Session with id: '%s' Not Found.", idSession);

        sessionRepository.delete(sessionDB);
        sessionDB.setEntityState(MovieSession.EntityState.DELETED);

        return sessionDB;
    }
}

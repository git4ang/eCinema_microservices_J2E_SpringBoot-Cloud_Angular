package ang.neggaw.movies.services;

import ang.neggaw.movies.entities.MovieSession;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

@RepositoryRestResource
public interface SessionService {
    Object createSession(MovieSession session);
    MovieSession getSession(long idSession);
    Collection<MovieSession> allSessions();
    Object updateSession(MovieSession session);
    Object deleteSession(long idSession);
}

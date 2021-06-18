package ang.neggaw.movies.services;

import ang.neggaw.movies.entities.MovieProjection;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Collection;

@RepositoryRestResource
public interface ProjectionService {
    Object createProjection(MovieProjection projection);
    Object getProjection(long idProj, boolean isFullProjection);
    Collection<MovieProjection> allProjections();
    Object updateProjection(MovieProjection projection);
    Object addProjectionToRoom(long idProj, long idRoom);
    Object deleteProjection(long idProj);
}

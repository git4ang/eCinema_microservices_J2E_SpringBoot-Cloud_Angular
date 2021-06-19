package ang.neggaw.movies.services.implementations;

import ang.neggaw.movies.beans.RoomProxy;
import ang.neggaw.movies.entities.Movie;
import ang.neggaw.movies.entities.MovieProjection;
import ang.neggaw.movies.entities.MovieSession;
import ang.neggaw.movies.proxies.RoomRestProxy;
import ang.neggaw.movies.repositories.MovieRepository;
import ang.neggaw.movies.repositories.ProjectionRepository;
import ang.neggaw.movies.services.ProjectionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Collection;
import java.util.Date;

@Log4j2
@RequiredArgsConstructor
@Service
public class ProjectionServiceImpl implements ProjectionService {
    
    private final ProjectionRepository projectionRepository;
    private final MovieRepository movieRepository;
    private final RoomRestProxy roomRestProxy;


    @Override
    public Object createProjection(MovieProjection p) {

        MovieProjection projectionDB = projectionRepository.findByDateProjectionAndIdMovieAndIdRoom(p.getDateProjection(), p.getIdMovie(), p.getIdRoom());
        if(projectionDB != null) return String.format("Projection already exists: '%s'.", p);

        // RoomProxy
        try {
            p.setRoom(roomRestProxy.getRoom(p.getIdRoom()).getBody());

            Movie movie = movieRepository.findByIdMovie(p.getIdMovie());
            if (movie == null) return String.format("Unable to create. Movie with id '%s' Not Found.", p.getIdMovie());
            p.setMovie(movie);

            p.setEntityState(MovieProjection.EntityState.CREATED);
            p.setDateProjection(new Date());
            projectionDB = projectionRepository.save(p);

            // adding and updating idProj to Room entity
            if ( ! projectionDB.getRoom().getIdsProjectionsRoom().contains(projectionDB.getIdProjection()) ) {
                projectionDB.getRoom().getIdsProjectionsRoom().add(projectionDB.getIdProjection());
                projectionDB.getRoom().setEntityState(MovieSession.EntityState.PROCESSING);
                roomRestProxy.updateRoom(projectionDB.getIdRoom(), projectionDB.getRoom());
            }

        } catch (Exception e) {
            return String.format(e.getMessage() + "\n\nUnable to create. Room with id: '%s' Not Found.", p.getIdRoom());
        }

        return projectionDB;
    }

    @Override
    public Object getProjection(long idProj, boolean isFullProjection) {

        MovieProjection movieProjection = projectionRepository.findByIdProjection(idProj);
        if(movieProjection == null) return String.format("Unable to update. Projection with id: '%s' Not Found.", idProj);

        if (isFullProjection && movieProjection.getRoom() == null)
            movieProjection.setRoom(roomRestProxy.getRoom(movieProjection.getIdRoom()).getBody());
        return movieProjection;
    }

    @Override
    public Collection<MovieProjection> allProjections() {
        Collection<MovieProjection> projections = projectionRepository.findAll();
        if(projections.isEmpty()) return null;
        return projections;
    }

    @Override
    public Object updateProjection(MovieProjection p) {

        MovieProjection projectionDB = (MovieProjection) getProjection(p.getIdProjection(), false);
        if( projectionDB == null) return String.format("Unable to update. Projection with id: '%s' Not Found.", p.getIdProjection());

        if (p.getEntityState() != null && p.getEntityState().equals(MovieProjection.EntityState.PROCESSING)) {
            p.setEntityState(MovieProjection.EntityState.UPDATED);
            return projectionRepository.saveAndFlush(p);
        }

        p.setEntityState(MovieProjection.EntityState.UPDATED);
        Movie movie = movieRepository.findByIdMovie(p.getIdMovie());
        if (movie == null) return String.format("Unable to update. Movie with id '%s' Not Found.", p.getIdMovie());
        p.setMovie(movie);

        p.setRoom(projectionDB.getRoom());
        p.setSessions(projectionDB.getSessions());
        return projectionRepository.saveAndFlush(p);
    }

    @Override
    public Object addProjectionToRoom(long idProj, long idRoom) {

        MovieProjection projection = projectionRepository.findByIdProjection(idProj);
        if(projection == null) return String.format("Unable to add Projection to Room. Projection with id: '%s' Not Found.", idProj);

        try {
            RoomProxy room = roomRestProxy.getRoom(idRoom).getBody();
            if( ! room.getIdsProjectionsRoom().contains(idProj)) {
                room.getIdsProjectionsRoom().add(idProj);
                room.setEntityState(MovieSession.EntityState.PROCESSING);
                roomRestProxy.updateRoom(idRoom, room);
            }
            projection.getRoom().getIdsProjectionsRoom().removeIf(id -> id == idProj);
            projection.getRoom().setEntityState(MovieSession.EntityState.PROCESSING);
            roomRestProxy.updateRoom(projection.getRoom().getIdRoom(), projection.getRoom());

            projection.setRoom(room);

        } catch (Exception e){
            return String.format(e.getMessage() + "\n\nUnable to add Projection with id: '%s' to Room with id: '%s'. Room Not Found.", idProj, idRoom);
        }

        return projection;
    }

    @Override
    public Object deleteProjection(long idProj) {

        MovieProjection projection = (MovieProjection) getProjection(idProj, true);
        if (projection == null) return String.format("Unable to delete. Projection with id: '%s' Not Found.", idProj);

        projection.getRoom().setEntityState(MovieSession.EntityState.PROCESSING);
        projection.getRoom().getIdsProjectionsRoom().removeIf(id -> id.equals(idProj));
        roomRestProxy.updateRoom(projection.getIdRoom(), projection.getRoom());

        projection.setEntityState(MovieProjection.EntityState.DELETED);
        projectionRepository.delete(projection);

        return projection;
    }
}

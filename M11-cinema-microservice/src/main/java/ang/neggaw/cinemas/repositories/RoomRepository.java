package ang.neggaw.cinemas.repositories;

import ang.neggaw.cinemas.entities.Cinema;
import ang.neggaw.cinemas.entities.Room;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface RoomRepository extends JpaRepository<Room, Long> {
    Room findByIdRoom(long idRoom);
    Room findByName(String name);
    Room findByNameAndAndCinema(String name, Cinema c);
}

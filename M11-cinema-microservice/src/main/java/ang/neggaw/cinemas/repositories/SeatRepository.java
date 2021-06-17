package ang.neggaw.cinemas.repositories;

import ang.neggaw.cinemas.entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface SeatRepository extends JpaRepository<Seat, Long> {
    Seat findByIdSeat(long idSeat);
    Seat findByRefSeat(String refSeat);
    Seat findByRefSeatAndIdRoom(String refSeat, long idRoom);
}

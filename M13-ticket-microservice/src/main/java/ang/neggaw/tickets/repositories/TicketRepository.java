package ang.neggaw.tickets.repositories;

import ang.neggaw.tickets.entities.MovieTicket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource
public interface TicketRepository extends JpaRepository<MovieTicket, Long> {
    MovieTicket findByIdTicket(long idTicket);
    MovieTicket findByIdSeatAndIdProj(long idSeat, long idProj);
}

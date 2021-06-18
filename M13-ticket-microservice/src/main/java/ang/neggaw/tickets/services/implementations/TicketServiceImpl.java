package ang.neggaw.tickets.services.implementations;

import ang.neggaw.tickets.beans.ProjectionProxy;
import ang.neggaw.tickets.beans.SeatProxy;
import ang.neggaw.tickets.entities.MovieTicket;
import ang.neggaw.tickets.proxies.ProjectionRestProxy;
import ang.neggaw.tickets.proxies.SeatRestProxy;
import ang.neggaw.tickets.repositories.TicketRepository;
import ang.neggaw.tickets.services.TicketService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
@Log4j2
public class TicketServiceImpl implements TicketService {

    private final TicketRepository ticketRepository;
    private final SeatRestProxy seatRestProxy;
    private final ProjectionRestProxy projectionRestProxy;

    @Override
    public Object createTicket(MovieTicket t) {

        MovieTicket ticketDB = ticketRepository.findByIdSeatAndIdProj(t.getIdSeat(), t.getIdProj());
        if (ticketDB != null) return String.format("Ticket with id: '%s' is already reserved.", ticketDB.getIdTicket());

        t.setEntityState(MovieTicket.EntityState.CREATED);
        t.setCodePayment(UUID.randomUUID().toString());

        // Adding Projection to Ticket
        try {
            t.setProjection(projectionRestProxy.getProjection(t.getIdProj()).getBody());

            // Adding Seat to Ticket
            try {
                t.setSeat(seatRestProxy.getSeat(t.getIdSeat()).getBody());
            } catch (Exception e) {
                return String.format(e.getMessage() + "\n\nUnable to create Ticket. Seat with id: '%s' Not Found.", t.getIdSeat());
            }
        } catch (Exception e){
            return String.format(e.getMessage() + "\n\nUnable to create Ticket. Projection with id: '%s' Not Found.", t.getIdProj());
        }

        ticketDB = ticketRepository.save(t);

        // adding & updating idTicket to Projection entity
        if ( ! ticketDB.getProjection().getIdsTicketsProjection().contains(ticketDB.getIdTicket())) {
            ticketDB.getProjection().getIdsTicketsProjection().add(ticketDB.getIdTicket());
            ticketDB.getProjection().setEntityState(MovieTicket.EntityState.PROCESSING);
            projectionRestProxy.updateProjection(ticketDB.getIdProj(), ticketDB.getProjection());
        }

        // adding & updating idTicket to Seat entity
        ticketDB.getSeat().setIdTicket(ticketDB.getIdTicket());
        ticketDB.getSeat().setEntityState(MovieTicket.EntityState.PROCESSING);
        seatRestProxy.updateSeat(ticketDB.getIdSeat(), ticketDB.getSeat());

        return ticketDB;
    }

    @Override
    public MovieTicket getTicket(long idTicket) { return ticketRepository.findByIdTicket(idTicket); }

    @Override
    public List<MovieTicket> allTickets() { return ticketRepository.findAll(); }

    @Override
    public Object updateTicket(MovieTicket t) {

        MovieTicket ticketDB = getTicket(t.getIdTicket());
        if(ticketDB == null) return String.format("Unable to update. Ticket with id: '%s' Not Found.", t.getIdTicket());

        t.setEntityState(MovieTicket.EntityState.UPDATED);
        t.setIdSeat(ticketDB.getIdSeat());
        t.setSeat(ticketDB.getSeat());
        t.setProjection(ticketDB.getProjection());
        t.setIdProj(ticketDB.getIdProj());

        return ticketRepository.saveAndFlush(t);
    }

    @Override
    public Object addTicketToProjection(long idTicket, long idProj) {

        MovieTicket ticketDB = ticketRepository.findByIdTicket(idTicket);
        if(ticketDB == null) return String.format("Unable to Add. movieTicket with id: '%s' Not Found.", idTicket);

        try {
            ProjectionProxy projection = projectionRestProxy.getProjection(idProj).getBody();

            ticketDB.setProjection(projectionRestProxy.getProjection(ticketDB.getIdProj()).getBody());
            ticketDB.getProjection().getIdsTicketsProjection().removeIf(id -> id.equals(ticketDB.getIdTicket()));
            ticketDB.getProjection().setEntityState(MovieTicket.EntityState.PROCESSING);
            projectionRestProxy.updateProjection(ticketDB.getIdProj(), ticketDB.getProjection());

            if(projection.getIdsTicketsProjection().contains(idTicket)) {
                projection.getIdsTicketsProjection().add(idTicket);
                projection.setEntityState(MovieTicket.EntityState.PROCESSING);
                projectionRestProxy.updateProjection(idProj, projection);
            }

            ticketDB.setProjection(projection);

        } catch (Exception e) {
            return String.format(e.getMessage() + "\n\nUnable to create Ticket. Projection with id: '%s' Not Found.", idProj);
        }

        return ticketRepository.saveAndFlush(ticketDB);
    }

    @Override
    public Object addTicketToSeat(long idTicket, long idSeat) {

        MovieTicket ticketDB = ticketRepository.findByIdTicket(idTicket);
        if(ticketDB == null) return String.format("Unable to Add. movieTicket with id: '%s' Not Found.", idTicket);

        try {
            SeatProxy seat = seatRestProxy.getSeat(idSeat).getBody();
            seat.setIdTicket(idTicket);
            seat.setTicket(ticketDB);
            seat.setEntityState(MovieTicket.EntityState.PROCESSING);
            seatRestProxy.updateSeat(idSeat, seat);

            ticketDB.setSeat(seatRestProxy.getSeat(ticketDB.getIdSeat()).getBody());
            ticketDB.getSeat().setIdTicket(0);
            ticketDB.getSeat().setTicket(null);
            ticketDB.getSeat().setEntityState(MovieTicket.EntityState.PROCESSING);
            seatRestProxy.updateSeat(ticketDB.getIdSeat(), ticketDB.getSeat());

            ticketDB.setIdSeat(idSeat);
            ticketDB.setSeat(seat);

        } catch (Exception e){
            return String.format(e.getMessage() + "\n\nUnable to create Ticket. Seat with id: '%s' Not Found.", idSeat);
        }

        return ticketRepository.saveAndFlush(ticketDB);
    }


    @Override
    @Transactional
    public Object deleteTicket(long idTicket) {

        MovieTicket ticketDB = getTicket(idTicket);
        if(ticketDB == null) return String.format("Unable to delete. movieTicket with id: '%s' Not Found.", idTicket);
        else {
            if (ticketDB.getProjection() != null) {
                ticketDB.getProjection().getIdsTicketsProjection().removeIf(id -> id.equals(idTicket));
                ticketDB.getProjection().setEntityState(MovieTicket.EntityState.PROCESSING);
                projectionRestProxy.updateProjection(ticketDB.getIdProj(), ticketDB.getProjection());
            }

            if (ticketDB.getSeat() != null) {
                ticketDB.getSeat().setIdTicket(0);
                ticketDB.getSeat().setTicket(null);
                ticketDB.getSeat().setEntityState(MovieTicket.EntityState.PROCESSING);
                seatRestProxy.updateSeat(ticketDB.getIdSeat(), ticketDB.getSeat());
            }
        }

        ticketRepository.delete(ticketDB);
        ticketDB.setEntityState(MovieTicket.EntityState.DELETED);

        return ticketDB;
    }
}

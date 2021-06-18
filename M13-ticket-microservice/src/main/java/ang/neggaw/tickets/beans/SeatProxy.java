package ang.neggaw.tickets.beans;

import ang.neggaw.tickets.entities.MovieTicket;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Transient;

@Data
public class SeatProxy {

    private Long idSeat;

    private String refSeat;

    private int rowSeat;

    private int columnSeat;

    @Enumerated(EnumType.STRING)
    private MovieTicket.EntityState entityState;

    private long idRoom;

    private long idTicket;

    @Transient
    private MovieTicket ticket;
}

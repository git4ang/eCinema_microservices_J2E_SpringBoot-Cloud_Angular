package ang.neggaw.movies.beans;

import ang.neggaw.movies.entities.MovieProjection;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class TicketProxy {

    private Long idTicket;

    private String CustomerName;

    private double price;

    private int codePayment;

    private boolean isReserved;

    @Enumerated(EnumType.STRING)
    private MovieProjection.EntityState entityState;

    private long idSeat;

    private long idProj;
}

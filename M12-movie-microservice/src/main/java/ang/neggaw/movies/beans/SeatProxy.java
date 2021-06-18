package ang.neggaw.movies.beans;

import ang.neggaw.movies.entities.MovieProjection;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
public class SeatProxy {

    private Long idSeat;

    private String refSeat;

    private int rowSeat;

    private int columnSeat;

    @Enumerated(EnumType.STRING)
    private MovieProjection.EntityState entityState;

    private long idRoom;

    private long idTicket;
}

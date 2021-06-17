package ang.neggaw.movies.beans;

import ang.neggaw.movies.entities.MovieSession;
import lombok.Data;

import javax.persistence.*;

@Data
public class RoomProxy {

    private Long idRoom;

    private String name;

    private int quantitySeats;

    @Enumerated(EnumType.STRING)
    private MovieSession.EntityState entityState;

    private long idCinema;
}
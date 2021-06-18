package ang.neggaw.movies.beans;

import ang.neggaw.movies.entities.MovieProjection;
import ang.neggaw.movies.entities.MovieSession;
import lombok.Data;
import lombok.ToString;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class RoomProxy {

    private Long idRoom;

    private String name;

    private int quantitySeats;

    @Enumerated(EnumType.STRING)
    private MovieSession.EntityState entityState;

    private long idCinema;

    Collection<SeatProxy> seats = new ArrayList<>();

    @ElementCollection
    private List<Long> idsProjectionsRoom = new ArrayList<>();

    @ToString.Exclude
    private Collection<MovieProjection> projections = new ArrayList<>();
}
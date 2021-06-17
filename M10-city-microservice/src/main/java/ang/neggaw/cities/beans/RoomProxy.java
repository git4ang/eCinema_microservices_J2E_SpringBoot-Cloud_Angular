package ang.neggaw.cities.beans;

import ang.neggaw.cities.entities.City;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;
import java.util.Collection;

@Data
public class RoomProxy {

    private Long idRoom;

    private String name;

    private int quantitySeats;

    @Enumerated(EnumType.STRING)
    private City.EntityState entityState;

    private long idCinema;

    @JsonIgnoreProperties(value = { "rooms" })
    private CinemaProxy cinema;

    @JsonIgnoreProperties(value = { "room" })
    private Collection<SeatProxy> seats;
}

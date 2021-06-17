package ang.neggaw.cities.beans;

import ang.neggaw.cities.entities.City;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
    private City.EntityState entityState;

    private long idRoom;

    @JsonIgnoreProperties(value = { "seats" })
    private RoomProxy room;
}

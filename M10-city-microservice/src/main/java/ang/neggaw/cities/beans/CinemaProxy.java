package ang.neggaw.cities.beans;

import ang.neggaw.cities.entities.City;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.Collection;
import java.util.List;

@Data
public class CinemaProxy {

    private Long idCinema;

    private String name;

    private String address;

    private long idCity;

    @Enumerated(EnumType.STRING)
    private City.EntityState entityState;

    private Collection<RoomProxy> rooms;

    private List<Long> idsCitiesCinema;

    private Collection<City> cities;
}
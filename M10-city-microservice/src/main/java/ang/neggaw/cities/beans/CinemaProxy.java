package ang.neggaw.cities.beans;

import ang.neggaw.cities.entities.City;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
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

    @JsonIgnoreProperties(value = {"cinema"})
    private Collection<RoomProxy> rooms = new ArrayList<>();

    private List<Long> idsCitiesCinema = new ArrayList<>();

    @JsonIgnoreProperties(value = { "cinema" })
    private Collection<City> cities = new ArrayList<>();
}
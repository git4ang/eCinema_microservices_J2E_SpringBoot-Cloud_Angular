package ang.neggaw.cinemas.beans;

import ang.neggaw.cinemas.entities.Cinema;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Data
public class CityProxy {

    private long idCity;

    private String name;

    private double longitude;

    private double latitude;

    private double altitude;

    @Enumerated(EnumType.STRING)
    private Cinema.EntityState entityState;

    private long idCinema;
    private List<Long> idsCinemasCity;
    private Collection<Cinema> cinemas = new ArrayList<>();
}

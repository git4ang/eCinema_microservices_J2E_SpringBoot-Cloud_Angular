package ang.neggaw.cities.entities;

import ang.neggaw.cities.beans.CinemaProxy;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "cities")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class City implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCity;

    @NonNull
    private String name;

    @NonNull
    private double longitude;

    @NonNull
    private double latitude;

    @NonNull
    private double altitude;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    private long idCinema;

    @ElementCollection
    @JoinTable(name = "cities_cinemas", joinColumns = @JoinColumn(name = "idCity"))
    private List<Long> idsCinemasCity;

    @Transient
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CinemaProxy cinema;

    @Transient
    private Collection<CinemaProxy> cinemas;

    //
    public City() {
        this.cinemas = new ArrayList<>();
        this.idsCinemasCity = new ArrayList<>();
    }
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}

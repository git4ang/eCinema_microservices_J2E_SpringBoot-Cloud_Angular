package ang.neggaw.cinemas.entities;

import ang.neggaw.cinemas.beans.CityProxy;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_cinemas")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class Cinema implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCinema;

    @NonNull
    private String name;

    @NonNull
    private String address;

    @NonNull
    private int numRooms;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
    private Collection<Room> rooms;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    @NonNull
    private long idCity;

    @Transient
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CityProxy city;

    @ElementCollection
    @JoinTable(name = "cinemas_cities", joinColumns = @JoinColumn(name = "idCinema"))
    private List<Long> idsCitiesCinema;

    @Transient
    @ToString.Exclude
    private Collection<CityProxy> cities;

    // Informations complementaires
    public Cinema (){
        this.rooms = new ArrayList<>();
        this.cities = new ArrayList<>();
        this.idsCitiesCinema = new ArrayList<>();
    }

    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}

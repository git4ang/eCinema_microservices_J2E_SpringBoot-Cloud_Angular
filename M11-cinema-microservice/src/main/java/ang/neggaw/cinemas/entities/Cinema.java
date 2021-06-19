package ang.neggaw.cinemas.entities;

import ang.neggaw.cinemas.beans.CityProxy;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
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
    @NotEmpty(message = "Name field cannot be empty")
    @NotNull(message = "Name field cannot be null")
    @NotBlank(message = "Name field cannot be blank")
    private String name;

    @NonNull
    @NotEmpty(message = "Address field cannot be empty")
    @NotNull(message = "Address field cannot be null")
    @NotBlank(message = "Address field cannot be blank")
    private String address;

    @NonNull
    @PositiveOrZero(message = "numRooms should be a positive number (n > 0)")
    private int numRooms;

    @OneToMany(mappedBy = "cinema", cascade = CascadeType.ALL)
    private Collection<Room> rooms;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    @NonNull
    @Min(value = 0, message = "idCity should not be less 0 (zero)")
    @NotNull(message = "idCity field cannot be null ")
    @PositiveOrZero(message = "idCity should be a positive number (n > 0)")
    private long idCity;

    @Transient
    @ToString.Exclude
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private CityProxy city;

    @ElementCollection
    @JoinTable(name = "cinemas_cities", joinColumns = @JoinColumn(name = "idCinema"))
    private List<@PositiveOrZero Long> idsCitiesCinema;

    @Transient
    @ToString.Exclude
    private Collection<CityProxy> cities;

    // Additional information
    public Cinema (){
        this.rooms = new ArrayList<>();
        this.cities = new ArrayList<>();
        this.idsCitiesCinema = new ArrayList<>();
    }

    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}

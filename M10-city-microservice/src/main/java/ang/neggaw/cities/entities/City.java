package ang.neggaw.cities.entities;

import ang.neggaw.cities.beans.CinemaProxy;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_cities")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@Builder
public class City implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCity;

    @NonNull
    @NotEmpty(message = "Name field cannot be empty")
    @NotNull(message = "Name field cannot be null")
    @NotBlank(message = "Name field cannot be blank")
    private String name;

    @NonNull
    @NotNull(message = "Longitude field cannot be null ")
    @Digits(message = "Longitude field must be a number (nn.dddd).", integer = 2, fraction = 4)
    private double longitude;

    @NonNull
    @NotNull(message = "Latitude field cannot be null ")
    @Digits(message = "Latitude field must be a number (nn.dddd).", integer = 4, fraction = 2)
    private double latitude;

    @NonNull
    @NotNull(message = "Altitude cannot be null ")
    @Digits(message = "Altitude field must be a number (nn.dddd).", integer = 4, fraction = 2)
    private double altitude;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    @Min(value = 0, message = "idCinema should not be less 0 (zero)")
    @NotNull(message = "idCinema field cannot be null ")
    @PositiveOrZero(message = "idCinema should be a positive number (n > 0)")
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

    // Additional information
    public City() {
        this.cinemas = new ArrayList<>();
        this.idsCinemasCity = new ArrayList<>();
    }
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}

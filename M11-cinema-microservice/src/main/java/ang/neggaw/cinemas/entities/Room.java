package ang.neggaw.cinemas.entities;

import ang.neggaw.cinemas.beans.ProjectionProxy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "tb_rooms")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
public class Room implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idRoom;

    @NonNull
    @NotEmpty(message = "Name field cannot be empty")
    @NotNull(message = "Name field cannot be null")
    @NotBlank(message = "Name field cannot be blank")
    private String name;

    @NonNull
    @PositiveOrZero(message = "quantitySeats should be a positive number (n > 0)")
    private int quantitySeats;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    @NonNull
    @Min(value = 0, message = "idCinema should not be less 0 (zero)")
    @NotNull(message = "idCinema field cannot be null ")
    @PositiveOrZero(message = "idCinema should be a positive number (n > 0)")
    private long idCinema;

    @ManyToOne
    @JoinColumn(name = "cinema", referencedColumnName = "idCinema")
    @JsonIgnoreProperties(value = { "rooms" })
    private Cinema cinema;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private Collection<Seat> seats;

    @Transient
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    @ToString.Exclude
    private long idProj;

    @ElementCollection
    @JoinTable(name = "rooms_projections", joinColumns = @JoinColumn(name = "idRoom"))
    private List<@PositiveOrZero Long> idsProjectionsRoom = new ArrayList<>();

    @Transient
    @ToString.Exclude
    private Collection<ProjectionProxy> projections = new ArrayList<>();


    // Additional information
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}

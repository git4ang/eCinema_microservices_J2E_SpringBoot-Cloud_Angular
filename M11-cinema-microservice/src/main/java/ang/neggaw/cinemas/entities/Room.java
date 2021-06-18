package ang.neggaw.cinemas.entities;

import ang.neggaw.cinemas.beans.ProjectionProxy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
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
    private String name;

    @NonNull
    private int quantitySeats;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    @NonNull
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
    private List<Long> idsProjectionsRoom;

    @Transient
    @ToString.Exclude
    private Collection<ProjectionProxy> projections;


    // Additional information
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}

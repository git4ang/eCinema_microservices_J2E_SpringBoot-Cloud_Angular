package ang.neggaw.cinemas.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Collection;

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
    private Cinema cinema;

    @OneToMany(mappedBy = "room", cascade = CascadeType.ALL)
    private Collection<Seat> seats;

    // Info complementaires
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}

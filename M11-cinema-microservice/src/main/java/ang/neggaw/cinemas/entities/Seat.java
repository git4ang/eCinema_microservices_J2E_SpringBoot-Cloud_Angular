package ang.neggaw.cinemas.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_seats")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
public class Seat implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSeat;

    @NonNull
    private String refSeat;

    @NonNull
    private int rowSeat;

    @NonNull
    private int columnSeat;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    @NonNull
    private long idRoom;

    @ManyToOne
    @JoinColumn(name = "room", referencedColumnName = "idRoom")
    @JsonIgnoreProperties(value = { "seats" })
    private Room room;

    ///// info complementaires /////
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}

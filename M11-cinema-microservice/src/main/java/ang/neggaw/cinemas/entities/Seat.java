package ang.neggaw.cinemas.entities;

import ang.neggaw.cinemas.beans.TicketProxy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
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
    @NotEmpty(message = "refSeat field cannot be empty")
    @NotNull(message = "refSeat field cannot be null")
    @NotBlank(message = "refSeat field cannot be blank")
    private String refSeat;

    @NonNull
    @Min(value = 0, message = "rowSeat should not be less 0 (zero)")
    @NotNull(message = "rowSeat field cannot be null ")
    @PositiveOrZero(message = "rowSeat should be a positive number (n > 0)")
    private int rowSeat;

    @NonNull
    @Min(value = 0, message = "columnSeat should not be less 0 (zero)")
    @NotNull(message = "columnSeat field cannot be null ")
    @PositiveOrZero(message = "columnSeat should be a positive number (n > 0)")
    private int columnSeat;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    @NonNull
    @Min(value = 0, message = "idRoom should not be less 0 (zero)")
    @NotNull(message = "idRoom field cannot be null ")
    @PositiveOrZero(message = "idRoom should be a positive number (n > 0)")
    private long idRoom;

    @ManyToOne
    @JoinColumn(name = "room", referencedColumnName = "idRoom")
    @JsonIgnoreProperties(value = { "seats" })
    private Room room;

    private long idTicket;

    @Transient
    private TicketProxy ticket;

    // Additional information
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}

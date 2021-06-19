package ang.neggaw.tickets.entities;


import ang.neggaw.tickets.beans.ProjectionProxy;
import ang.neggaw.tickets.beans.SeatProxy;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;

@Entity
@Table(name = "tb_tickets")
@Data
@AllArgsConstructor
@NoArgsConstructor
@RequiredArgsConstructor
@Builder
public class MovieTicket implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idTicket;

    @NonNull
    @NotEmpty(message = "customerName field cannot be empty")
    @NotNull(message = "customerName field cannot be null")
    @NotBlank(message = "customerName field cannot be blank")
    private String customerName;

    @NonNull
    @Digits(integer = 2, fraction = 2)
    private double price;

    @NonNull
    private String codePayment;

    @NonNull
    private boolean reserved;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    @NonNull
    @Min(value = 0, message = "idSeat should not be less 0 (zero)")
    @NotNull(message = "idSeat field cannot be null ")
    @PositiveOrZero(message = "idSeat should be a positive number (n > 0)")
    private long idSeat;

    @Transient
    private SeatProxy seat;

    @NonNull
    @Min(value = 0, message = "idProj should not be less 0 (zero)")
    @NotNull(message = "idProj field cannot be null ")
    @PositiveOrZero(message = "idProj should be a positive number (n > 0)")
    private long idProj;

    @Transient
    private ProjectionProxy projection;

    // Additional Information
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}
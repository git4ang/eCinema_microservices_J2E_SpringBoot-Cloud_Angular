package ang.neggaw.tickets.entities;


import ang.neggaw.tickets.beans.ProjectionProxy;
import ang.neggaw.tickets.beans.SeatProxy;
import lombok.*;

import javax.persistence.*;
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
    private String customerName;

    @NonNull
    private double price;

    @NonNull
    private String codePayment;

    @NonNull
    private boolean reserved;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    @NonNull
    private long idSeat;

    @Transient
    private SeatProxy seat;

    @NonNull
    private long idProj;

    @Transient
    private ProjectionProxy projection;

    // Additional Information
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}
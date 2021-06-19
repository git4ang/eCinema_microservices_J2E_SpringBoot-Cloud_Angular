package ang.neggaw.movies.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "tb_sessions")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
public class MovieSession implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idSession;

    @NonNull
    @Temporal(TemporalType.TIME)
    private Date movieStartTime;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    @NonNull
    @Min(value = 0, message = "idProjection should not be less 0 (zero)")
    @NotNull(message = "idProjection field cannot be null ")
    @PositiveOrZero(message = "idProjection should be a positive number (n > 0)")
    private long idProjection;

    @ManyToOne
    @JsonIgnoreProperties(value = {"sessions"})
    private MovieProjection projection;

    // Additional information
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}

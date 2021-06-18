package ang.neggaw.movies.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
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

    private Date movieStartTime;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    @NonNull
    private long idProjection;

    @ManyToOne
    @JsonIgnoreProperties(value = {"sessions"})
    private MovieProjection projection;

    // Additional information
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}

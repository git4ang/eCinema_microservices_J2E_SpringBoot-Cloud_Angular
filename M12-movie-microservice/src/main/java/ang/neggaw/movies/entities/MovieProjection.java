package ang.neggaw.movies.entities;

import ang.neggaw.movies.beans.RoomProxy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Digits;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_projections")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
public class MovieProjection implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProjection;

    @Temporal(TemporalType.DATE)
    private Date dateProjection;

    @NonNull
    @Digits(integer = 2, fraction = 2)
    private double price;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    @NonNull
    @Min(value = 0, message = "idMovie should not be less 0 (zero)")
    @NotNull(message = "idMovie field cannot be null ")
    @PositiveOrZero(message = "idMovie should be a positive number (n > 0)")
    private long idMovie;

    @ManyToOne
    @JoinColumn(name = "movie", referencedColumnName = "idMovie")
    @JsonIgnoreProperties(value = {"projections", "category"})
    private Movie movie;

    @JsonIgnoreProperties(value = {"projection"})
    @OneToMany(mappedBy = "projection", cascade = {CascadeType.ALL})
    private Collection<MovieSession> sessions = new ArrayList<>();

    @NonNull
    @Min(value = 0, message = "idRoom should not be less 0 (zero)")
    @NotNull(message = "idRoom field cannot be null ")
    @PositiveOrZero(message = "idRoom should be a positive number (n > 0)")
    private long idRoom;

    @Transient
    private RoomProxy room;

    // Additional information
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }

}
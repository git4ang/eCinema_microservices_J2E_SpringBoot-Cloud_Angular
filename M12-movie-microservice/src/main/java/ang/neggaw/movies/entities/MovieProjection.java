package ang.neggaw.movies.entities;

import ang.neggaw.movies.beans.RoomProxy;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
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
public class MovieProjection {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idProjection;

    private Date dateProjection;

    @NonNull
    private double price;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    @NonNull
    private long idMovie;

    @ManyToOne
    @JoinColumn(name = "movie", referencedColumnName = "idMovie")
    @JsonIgnoreProperties(value = {"projections", "category"})
    private Movie movie;

    @JsonIgnoreProperties(value = {"projection"})
    @OneToMany(mappedBy = "projection", cascade = {CascadeType.ALL})
    private Collection<MovieSession> sessions;

    @NonNull
    private long idRoom;

    @Transient
    private RoomProxy room;

    // Additional information
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }

}
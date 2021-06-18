package ang.neggaw.movies.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "tb_movies")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
public class Movie implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idMovie;

    private String title;

    @NonNull
    private double runningTime;

    @NonNull
    private String director;

    @ElementCollection
    private List<String> actors;

    @ElementCollection
    private List<String> languages;

    @NonNull
    private String description;

    @NonNull
    private String photo;

    @Temporal(TemporalType.DATE)
    private Date releaseDate;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    @NonNull
    @Transient
    private long idCategory;

    @ManyToOne
    @JoinColumn(name = "Category", referencedColumnName = "idCategory")
    private Category category;

    @OneToMany(mappedBy = "movie")
    Collection<MovieProjection> projections = new ArrayList<>();

    // Additional information
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}
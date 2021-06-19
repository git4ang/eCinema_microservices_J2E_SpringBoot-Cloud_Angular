package ang.neggaw.movies.entities;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.*;
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

    @NotEmpty(message = "title field cannot be empty")
    @NotNull(message = "title field cannot be null")
    @NotBlank(message = "title field cannot be blank")
    private String title;

    @NonNull
    private double runningTime;

    @NonNull
    @NotEmpty(message = "director field cannot be empty")
    @NotNull(message = "director field cannot be null")
    @NotBlank(message = "director field cannot be blank")
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
    @Min(value = 0, message = "idCategory should not be less 0 (zero)")
    @NotNull(message = "idCategory field cannot be null ")
    @PositiveOrZero(message = "idCategory should be a positive number (n > 0)")
    private long idCategory;

    @ManyToOne
    @JoinColumn(name = "Category", referencedColumnName = "idCategory")
    private Category category;

    @OneToMany(mappedBy = "movie")
    Collection<MovieProjection> projections = new ArrayList<>();

    // Additional information
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}
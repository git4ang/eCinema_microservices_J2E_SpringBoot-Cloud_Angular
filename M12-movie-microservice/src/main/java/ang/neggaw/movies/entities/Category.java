package ang.neggaw.movies.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.util.Collection;

@Entity
@Table(name = "tb_categories")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCategory;

    @NonNull
    @Column(unique = true)
    private String name;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    @JsonIgnoreProperties(value = {"category"})
    @OneToMany(mappedBy = "category", cascade = CascadeType.ALL)
    private Collection<Movie> movies;

    // Additional information
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}
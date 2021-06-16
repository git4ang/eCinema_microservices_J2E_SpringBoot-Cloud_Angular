package ang.neggaw.cities.entities;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "cities")
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Builder
public class City implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCity;

    @NonNull
    private String name;

    @NonNull
    private double longitude;

    @NonNull
    private double latitude;

    @NonNull
    private double altitude;

    @Enumerated(EnumType.STRING)
    private EntityState entityState;

    //
    public enum EntityState { CREATED, UPDATED, DELETED, PROCESSING }
}

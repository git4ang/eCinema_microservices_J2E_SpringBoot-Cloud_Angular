package ang.neggaw.cinemas.beans;

import ang.neggaw.cinemas.entities.Room;
import lombok.Data;

import javax.persistence.*;
import java.util.Date;

@Data
public class ProjectionProxy {

    private Long idProjection;

    private Date dateProjection;

    private double price;

    @Enumerated(EnumType.STRING)
    private Room.EntityState entityState;

    private long idMovie;

    private long idRoom;

    private Room room;
}

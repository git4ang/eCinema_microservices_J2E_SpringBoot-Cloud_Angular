package ang.neggaw.tickets.beans;

import ang.neggaw.tickets.entities.MovieTicket;
import lombok.Data;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Data
public class ProjectionProxy {

    private Long idProjection;

    private Date dateProjection;

    private double price;

    @Enumerated(EnumType.STRING)
    private MovieTicket.EntityState entityState;

    private long idMovie;

    private long idRoom;

    private long idTicket;
    private MovieTicket ticket;
    private List<Long> idsTicketsProjection = new ArrayList<>();
    private Collection<MovieTicket> tickets = new ArrayList<>();
}

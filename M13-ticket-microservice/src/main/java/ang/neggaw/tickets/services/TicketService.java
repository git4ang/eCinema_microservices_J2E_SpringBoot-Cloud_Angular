package ang.neggaw.tickets.services;

import ang.neggaw.tickets.entities.MovieTicket;

import java.util.Collection;

public interface TicketService {
    Object createTicket(MovieTicket t);
    MovieTicket getTicket(long idTicket);
    Collection<MovieTicket> allTickets();
    Object updateTicket(MovieTicket t);
    Object addTicketToProjection(long idTicket, long idProj);
    Object addTicketToSeat(long idTicket, long idSeat);
    Object deleteTicket(long idTicket);
}

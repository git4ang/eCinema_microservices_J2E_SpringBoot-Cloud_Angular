package ang.neggaw.cinemas.services;

import ang.neggaw.cinemas.entities.Seat;

import java.util.Collection;

public interface SeatService {
    Object createSeat(Seat s);
    Seat getSeat(long idSeat);
    Collection<Seat> allSeats();
    Object updateSeat(Seat s);
    Seat deleteSeat(long idSeat);
}

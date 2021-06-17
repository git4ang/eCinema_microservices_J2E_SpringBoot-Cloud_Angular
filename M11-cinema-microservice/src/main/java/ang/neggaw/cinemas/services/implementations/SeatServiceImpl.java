package ang.neggaw.cinemas.services.implementations;

import ang.neggaw.cinemas.entities.Seat;
import ang.neggaw.cinemas.repositories.SeatRepository;
import ang.neggaw.cinemas.services.SeatService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Log4j2
@AllArgsConstructor
@Service
public class SeatServiceImpl implements SeatService {

    private final SeatRepository seatRepository;


    @Override
    public Object createSeat(Seat s) {
        return null;
    }

    @Override
    public Seat getSeat(long idSeat) {
        return null;
    }

    @Override
    public Collection<Seat> allSeats() {
        return null;
    }

    @Override
    public Object updateSeat(Seat s) {
        return null;
    }

    @Override
    public Seat deleteSeat(long idSeat) {
        return null;
    }
}

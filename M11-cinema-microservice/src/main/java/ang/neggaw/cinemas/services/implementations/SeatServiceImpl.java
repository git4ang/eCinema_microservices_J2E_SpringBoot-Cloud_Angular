package ang.neggaw.cinemas.services.implementations;

import ang.neggaw.cinemas.entities.Room;
import ang.neggaw.cinemas.entities.Seat;
import ang.neggaw.cinemas.repositories.RoomRepository;
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
    private final RoomRepository roomRepository;


    @Override
    public Object createSeat(Seat s) {

        Room room = roomRepository.findByIdRoom(s.getIdRoom());
        if (room == null) return String.format("Room with id: '%s' Not Found.", s.getIdRoom());

        Seat seatDB = seatRepository.findByRefSeatAndIdRoom(s.getRefSeat(), room.getIdRoom());
        if (seatDB != null) return String.format("Seat with this ref. '%s' and Room entity id: '%s' already exists.", s.getRefSeat(), s.getIdRoom());

        s.setEntityState(Seat.EntityState.CREATED);
        s.setRoom(room);
        return seatRepository.save(s);
    }

    @Override
    public Seat getSeat(long idSeat) { return seatRepository.findByIdSeat(idSeat); }

    @Override
    public Collection<Seat> allSeats() { return seatRepository.findAll(); }

    @Override
    public Object updateSeat(Seat s) {
        
        Seat seatDB = getSeat(s.getIdSeat());
        if(seatDB == null) return String.format("Seat with id: '%s' Not Found.", s.getIdSeat());

        if(s.getEntityState() != null && s.getEntityState().equals(Seat.EntityState.PROCESSING)) {
            s.setEntityState(Seat.EntityState.UPDATED);
            return seatRepository.saveAndFlush(s);
        }

        s.setEntityState(Seat.EntityState.UPDATED);
        s.setRoom(seatDB.getRoom());
        return seatRepository.saveAndFlush(s);
    }

    @Override
    public Object deleteSeat(long idSeat) {
        
        Seat seatDB = seatRepository.findByIdSeat(idSeat);
        if(seatDB == null) return String.format("Seat with id: '%s' Not Found.", idSeat);

        seatRepository.delete(seatDB);
        seatDB.setEntityState(Seat.EntityState.DELETED);

        return seatDB;
    }
}

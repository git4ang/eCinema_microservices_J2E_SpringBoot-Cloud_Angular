package ang.neggaw.cinemas.services.implementations;

import ang.neggaw.cinemas.entities.Cinema;
import ang.neggaw.cinemas.entities.Room;
import ang.neggaw.cinemas.entities.Seat;
import ang.neggaw.cinemas.proxies.ProjectionRestProxy;
import ang.neggaw.cinemas.repositories.CinemaRepository;
import ang.neggaw.cinemas.repositories.RoomRepository;
import ang.neggaw.cinemas.repositories.SeatRepository;
import ang.neggaw.cinemas.services.RoomService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Log4j2
@AllArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomRepository roomRepository;
    private final CinemaRepository cinemaRepository;
    private final SeatRepository seatRepository;
    private final ProjectionRestProxy projectionRestProxy;


    @Override
    public Object createRoom(Room r) {

        Cinema cinema = cinemaRepository.findByIdCinema(r.getIdCinema());
        if (cinema == null) return String.format("Cinema with id: '%s' Not Found.", r.getIdCinema());

        Room roomDB = roomRepository.findByNameAndIdCinema(r.getName(), cinema.getIdCinema());
        if (roomDB != null) return String.format("Room already exists with name: '%s' & Cinema with id: '%s'.", r.getName(), r.getIdCinema());

        r.setEntityState(Room.EntityState.CREATED);
        r.setCinema(cinema);
        return roomRepository.save(r);
    }

    @Override
    public Room getRoom(long idRoom, boolean isFullRoom) {

        Room room = roomRepository.findByIdRoom(idRoom);
        if(isFullRoom && room != null && ! room.getIdsProjectionsRoom().isEmpty())
            room.getIdsProjectionsRoom().forEach(id -> room.getProjections().add(projectionRestProxy.getProjection(id).getBody()));
        return room;
    }

    @Override
    public Collection<Room> allRooms() {
        return roomRepository.findAll();
    }

    @Override
    public Object updateRoom(Room r) {

        Room roomDB = roomRepository.findByIdRoom(r.getIdRoom());
        if(roomDB == null) return String.format("Room with id: '%s' Not Found.", r.getIdRoom());

        if (r.getEntityState() != null && r.getEntityState().equals(Room.EntityState.PROCESSING)) {
            r.setEntityState(Room.EntityState.UPDATED);
            return roomRepository.saveAndFlush(r);
        }

        r.setEntityState(Room.EntityState.UPDATED);
        r.setCinema(roomDB.getCinema());
        r.setSeats(roomDB.getSeats());
        return roomRepository.saveAndFlush(r);
    }

    @Override
    public Object addRoomToCinema(long idRoom, long idCinema) {

        Cinema c = cinemaRepository.findByIdCinema(idCinema);
        if (c == null) return String.format("Cinema with id: '%s' Not Found.", idCinema);

        Room r = roomRepository.findByIdRoom(idRoom);
        if (r == null) return String.format("Room with id: '%s' Not Found.", idRoom);

        if ( ! c.getRooms().contains(r)) c.getRooms().add(r);
        if ( ! r.getCinema().equals(c)) r.setCinema(c);

        cinemaRepository.saveAndFlush(c);
        return roomRepository.saveAndFlush(r);
    }

    @Override
    public Object removeSeatFromRoom(long idSeat, long idRoom) {

        Seat s = seatRepository.findByIdSeat(idSeat);
        if (s == null) return String.format("Seat with id: '%s' Not Found.", idSeat);

        Room r = roomRepository.findByIdRoom(idRoom);
        if (r == null) return String.format("Room with id: '%s' Not Found.", idRoom);

        if ( ! r.getSeats().contains(s)) {

            r.getSeats().removeIf(seat -> seat == s);
            seatRepository.saveAndFlush(s);

            s.setRoom(r);
        }
        else return String.format("Seat with id: '%s' Not Found or Not Belong to Room with id: '%s'", idSeat, idRoom);

        return roomRepository.saveAndFlush(r);
    }

    @Override
    public Object deleteRoom(long idRoom) {
        
        Room roomDB = getRoom(idRoom, true);
        if(roomDB == null) return String.format("Room with id: '%s' Not Found.", idRoom);

        if( ! roomDB.getProjections().isEmpty()) {
            roomDB.getProjections().forEach(proj -> {
                proj.setRoom(null);
                proj.setEntityState(Room.EntityState.PROCESSING);
                projectionRestProxy.updateCategory(proj.getIdProjection(), proj);
                projectionRestProxy.deleteProjection(proj.getIdProjection());
            });
        }

        roomRepository.delete(roomDB);
        roomDB.setEntityState(Room.EntityState.DELETED);

        return roomDB;
    }
}

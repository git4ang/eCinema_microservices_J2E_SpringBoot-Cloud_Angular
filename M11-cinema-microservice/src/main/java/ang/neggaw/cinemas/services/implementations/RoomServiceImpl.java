package ang.neggaw.cinemas.services.implementations;

import ang.neggaw.cinemas.entities.Room;
import ang.neggaw.cinemas.repositories.RoomRepository;
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


    @Override
    public Object createRoom(Room r) {
        return null;
    }

    @Override
    public Room getRoom(long idRoom) {
        return null;
    }

    @Override
    public Collection<Room> allRooms() {
        return null;
    }

    @Override
    public Object updateRoom(Room r) {
        return null;
    }

    @Override
    public Object deleteRoom(long idRoom) {
        return null;
    }
}

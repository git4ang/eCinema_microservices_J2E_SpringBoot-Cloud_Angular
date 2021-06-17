package ang.neggaw.cinemas.services;

import ang.neggaw.cinemas.entities.Room;

import java.util.Collection;

public interface RoomService {
    Object createRoom(Room r);
    Room getRoom(long idRoom);
    Collection<Room> allRooms();
    Object updateRoom(Room r);
    Object deleteRoom(long idRoom);
}

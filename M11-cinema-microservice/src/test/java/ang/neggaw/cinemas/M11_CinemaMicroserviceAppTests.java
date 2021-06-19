package ang.neggaw.cinemas;

import ang.neggaw.cinemas.entities.Cinema;
import ang.neggaw.cinemas.entities.Room;
import ang.neggaw.cinemas.entities.Seat;
import ang.neggaw.cinemas.services.CinemaService;
import ang.neggaw.cinemas.services.RoomService;
import ang.neggaw.cinemas.services.SeatService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


////////////////////////////////////////////////////////////////////////////////////////////////
///       The project 'M10-City-microservice' must be started for everything to work        ///
////////////////////////////////////////////////////////////////////////////////////////////////


@Log4j2
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
class M11_CinemaMicroserviceAppTests {

    @Autowired
    private CinemaService cinemaService;

    @Autowired
    private RoomService roomService;

    @Autowired
    private SeatService seatService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    /// ****************************************************************** ///
    /// *** POST method Entities: Cinema, Room & Seat                *** ///
    /// ****************************************************************** ///
    @Test
    @Order(value = 1)
    @DisplayName(value = "POST:1 (createCinema) => /api/cinemas")
    void createCinemaTest() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing POST method in Cinema entity (createCinema) *");
        log.info("*******************************************************");

        Cinema cinemaTest = new Cinema("cinema_01", "Rue xxx, cityX", 6, 1);
        mockMvc.perform(post("/api/cinemas")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cinemaTest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Order(value = 2)
    @DisplayName(value = "POST:2 (createRoom) => /api/rooms")
    void createRoomTest() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing POST method in Room entity (createRoom)   *");
        log.info("*******************************************************");

        Room roomTest = new Room("room_01", 40, 1);
        mockMvc.perform(post(("/api/rooms"))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(roomTest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Order(value = 3)
    @DisplayName(value = "POST:3 (createSeat) => /api/seats")
    void createSeatTest() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing POST method in Seat entity (createSeat)   *");
        log.info("*******************************************************");

        Seat seatTest = new Seat("refSeat_01", 1, 1, 1);
        mockMvc.perform(post("/api/seats")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(seatTest)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    /// ****************************************************************** ///
    /// *** GET method (all) Entities: Cinema, Room & Seat           *** ///
    /// ****************************************************************** ///
    @Test
    @Order(4)
    @DisplayName(value = "GET:4 (getAllCinemas) => /api/cinemas")
    void getAllCinemasTest() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing GET method in Cinema entity (getAllCinemas) *");
        log.info("*******************************************************");

        Stream.of(
                new Cinema("cinema_02", "Rue xxx, City X", 6,2),
                new Cinema("cinema_03", "Rue xxx, City X", 12, 1),
                new Cinema("cinema_04", "Rue xxx, City X", 18, 2)
        ).forEach(cinema -> cinemaService.createCinema(cinema));

        mockMvc.perform(get("/api/cinemas")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[*].idCinema").isNotEmpty())
                .andExpect(jsonPath("$.size()", is(6)));
    }

    @Test
    @Order(5)
    @DisplayName(value = "GET:5 (getAllRooms) => /api/rooms")
    void getAllRoomsTest() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing GET method in Room entity (getAllRooms)   *");
        log.info("*******************************************************");

        Stream.of(
                new Room("room_02", 25, 2),
                new Room("room_03", 15, 1),
                new Room("room_04", 35, 1)
        ).forEach(room -> roomService.createRoom(room));

        mockMvc.perform(get("/api/rooms")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[*].idRoom").isNotEmpty())
                .andExpect(jsonPath("$.size()", is(6)));
    }

    @Test
    @Order(6)
    @DisplayName(value = "GET:6 (getAllSeats) => /api/seats")
    void getAllSeatTest() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing GET method in Seat entity (getAllSeats)   *");
        log.info("*******************************************************");

        Stream.of(
                new Seat("refSeat_02", 1, 2, 1),
                new Seat("refSeat_03", 2, 1, 1),
                new Seat("refSeat_04", 2, 2, 1)
        ).forEach(seat -> seatService.createSeat(seat));

        mockMvc.perform(get("/api/seats")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[*].idSeat").isNotEmpty())
                .andExpect(jsonPath("$.size()", is(6)));

    }

    /// ****************************************************************** ///
    /// *** GET method (byId) Entities: Cinema, Room & Seat          *** ///
    /// ****************************************************************** ///
    @Test
    @Order(7)
    @DisplayName(value = "GET:7 (getByIdCinema) => /api/cinemas/{idCinema}")
    void getByIdCinemaTest() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing GET method in Cinema entity (getByIdCinema) *");
        log.info("*******************************************************");

        mockMvc.perform(get("/api/cinemas/{idCinema}", 3)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("cinema_01")));
    }

    @Test
    @Order(8)
    @DisplayName(value = "GET:8 (getByIdRoom) => /api/rooms/{idRoom}")
    void getByIdRoomTest() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing GET method in Room entity (getByIdRoom)     *");
        log.info("*******************************************************");

        mockMvc.perform(get("/api/rooms/{idRoom}", 3)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("room_01")));

    }

    @Test
    @Order(9)
    @DisplayName(value = "GET:9 (getByIdSeat) => /api/seats/{idSeat}")
    void getByIdSeatTest() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing GET method in Seat entity (getByIdSeat)     *");
        log.info("*******************************************************");

        mockMvc.perform(get("/api/seats/{idSeat}", 3)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.refSeat", is("refSeat_01")));

    }

    /// ******************************************************************** ///
    /// *** UPDATE method. Entities: Cinema, Room & Seat               *** ///
    /// ******************************************************************** ///
    @Test
    @Order(10)
    @DisplayName(value = "PUT:10 (updateCinema) => /api/cinemas/{idCinema}")
    void updateCinemaTest() throws Exception {

        log.info("*********************************************************");
        log.info("* Testing UPDATE method in Cinema entity (updateCinema) *");
        log.info("*********************************************************");

        mockMvc.perform(put("/api/cinemas/{idCinema}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Cinema("cinema_02_up", "Rue yyy City Y", 24, 1))))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @Test
    @Order(11)
    @DisplayName(value = "PUT:11 (updateRoom) => /api/rooms/{idRoom}")
    void updateRoomTest() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing UPDATE method in Room entity (updateRoom) *");
        log.info("*******************************************************");

        mockMvc.perform(put("/api/rooms/{idRoom}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Room("room_02_up", 7, 1))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idCinema", is(1)))
                .andExpect(jsonPath("$.entityState", is("UPDATED")));
    }

    @Test
    @Order(12)
    @DisplayName(value = "PUT:12 (updateSeat) => /api/seats/{idSeat}")
    void updateSeatTest() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing UPDATE method in Seat entity (updateSeat) *");
        log.info("*******************************************************");

        mockMvc.perform(put("/api/seats/{idSeat}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(new Seat("refSeat_02_up", 7, 7, 1))))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idTicket", is(0)))
                .andExpect(jsonPath("$.entityState", is("UPDATED")));
    }

    /// ****************************************************************** ///
    /// *** DELETE method. Entities: Cinema, Room & Seat)            *** ///
    /// ****************************************************************** ///
    @Test
    @Order(13)
    @DisplayName(value = "DELETE:13 (deleteCinema) => /api/cinemas/{idCinema}")
    void deleteCinema() throws Exception {

        log.info("*********************************************************");
        log.info("* Testing DELETE method in Cinema entity (deleteCinema) *");
        log.info("*********************************************************");

        mockMvc.perform(delete("/api/cinemas/{idCinema}", 4)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    @Order(14)
    @DisplayName(value = "DELETE:14 (deleteRoom) => /api/rooms/{idRoom}")
    void deleteRoom() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing DELETE method in Room entity (deleteRoom) *");
        log.info("*******************************************************");

        mockMvc.perform(delete("/api/rooms/{idRoom}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    @Order(15)
    @DisplayName(value = "DELETE:15 (deleteSeat) => /api/seats/{idSeat}")
    void deleteSeat() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing DELETE method in Seat entity (deleteSeat) *");
        log.info("*******************************************************");

        mockMvc.perform(delete("/api/seats/{idSeat}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
    }
}

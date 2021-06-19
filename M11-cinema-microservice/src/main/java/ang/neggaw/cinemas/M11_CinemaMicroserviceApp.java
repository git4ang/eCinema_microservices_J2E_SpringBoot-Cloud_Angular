package ang.neggaw.cinemas;

import ang.neggaw.cinemas.entities.Cinema;
import ang.neggaw.cinemas.entities.Room;
import ang.neggaw.cinemas.entities.Seat;
import ang.neggaw.cinemas.services.CinemaService;
import ang.neggaw.cinemas.services.RoomService;
import ang.neggaw.cinemas.services.SeatService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;

@EnableEurekaClient
@EnableFeignClients
@SpringBootApplication
public class M11_CinemaMicroserviceApp {
    public static void main(String[] args) {
        SpringApplication.run(M11_CinemaMicroserviceApp.class, args);
    }

    @Bean
    CommandLineRunner start(CinemaService cinemaService,
                            RoomService roomService,
                            SeatService seatService,
                            RepositoryRestConfiguration restConfiguration) {


        restConfiguration.exposeIdsFor(Cinema.class, Room.class, Seat.class);

        return args -> {
            cinemaService.createCinema(new Cinema("cinemaTest_01", "Rue xxx Ville X", 17, 1));
            cinemaService.createCinema(new Cinema("cinemaTest_02", "Rue xxx Ville X", 17, 2));

            roomService.createRoom(new Room("roomTest_01", 20, 1));
            roomService.createRoom(new Room("roomTest_02", 20, 2));

            seatService.createSeat(new Seat("refSeatTest_01", 1, 1, 1));
            seatService.createSeat(new Seat("refSeatTest_02", 1, 2, 1));
        };
    }
}

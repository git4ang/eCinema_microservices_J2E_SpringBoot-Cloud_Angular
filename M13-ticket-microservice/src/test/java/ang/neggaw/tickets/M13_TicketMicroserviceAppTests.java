package ang.neggaw.tickets;

import ang.neggaw.tickets.entities.MovieTicket;
import ang.neggaw.tickets.services.TicketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;
import java.util.stream.Stream;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/////////////////////////////////////////////////////////////////////////////////////////////////
///       The project 'M10-city-microservice' must be started for everything to work         ///
///       The project 'M11-cinema-microservice' must be started for everything to work        ///
///       The project 'M12-movie-microservice' must be started for everything to work          ///
/////////////////////////////////////////////////////////////////////////////////////////////////

@Log4j2
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
class M13_TicketMicroserviceAppTests {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    /// ******************************************************************* ///
    /// *** POST method Entity: Ticket                                  *** ///
    /// ******************************************************************* ///
    @Test
    @Order(1)
    @DisplayName("POST-1 method: createTicket -> /api/tickets")
    void createTicketTest() throws Exception {

        log.info("**************************************************************");
        log.info("* Testing POST method in Ticket entity (createTicket)        *");
        log.info("**************************************************************");

        MovieTicket ticket = new MovieTicket("customerName_01", 15, UUID.randomUUID().toString(),true,1,1);
        mockMvc.perform(post("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticket)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    /// ******************************************************************* ///
    /// *** GETall method Entity: Ticket                                *** ///
    /// ******************************************************************* ///
    @Test
    @Order(2)
    @DisplayName("GETall-2 method: getAllTickets -> /api/tickets")
    void getAllTicketsTest() throws Exception {

        log.info("**************************************************************");
        log.info("* Testing ALL method in Ticket entity (getAllTickets)        *");
        log.info("**************************************************************");

        Stream.of(
                new MovieTicket("customerName_02", 15, UUID.randomUUID().toString(),true,2,1),
                new MovieTicket("customerName_03", 20, UUID.randomUUID().toString(),true,2,2),
                new MovieTicket("customerName_04", 10, UUID.randomUUID().toString(),true,1,2)
        ).forEach(ticket_cinema -> ticketService.createTicket(ticket_cinema));

        mockMvc.perform(get("/api/tickets")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$.[*].idTicket").isNotEmpty())
                .andExpect(jsonPath("$.size()").value(4));
    }

    /// ******************************************************************* ///
    /// *** POST method Entity: Ticket                                  *** ///
    /// ******************************************************************* ///
    @Test
    @Order(3)
    @DisplayName("GET-3 method: getTicketById -> /api/tickets/{idTicket}")
    void getTicketByIdTest() throws Exception {

        log.info("**************************************************************");
        log.info("* Testing GET method in Ticket entity (getTicketById)        *");
        log.info("**************************************************************");

        mockMvc.perform(get("/api/tickets/{idTicket}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerName").value("customerName_01"))
                .andExpect(jsonPath("$.reserved").value(true))
                .andExpect(jsonPath("$.price").value(15));
    }

    /// ******************************************************************* ///
    /// *** UPDATE method Entity: Ticket                                  *** ///
    /// ******************************************************************* ///
    @Test
    @Order(4)
    @DisplayName("UPDATE-4 method: updateTicket -> /api/tickets/{idTicket}")
    void updateTicketTest() throws Exception {

        log.info("**************************************************************");
        log.info("* Testing UPDATE method in Ticket entity (updateTicket)      *");
        log.info("**************************************************************");

        MovieTicket ticketUpdated = new MovieTicket("customerName_01_up", 99, UUID.randomUUID().toString(),false,1,1);
        mockMvc.perform(put("/api/tickets/{idTicket}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(ticketUpdated)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entityState").value("UPDATED"))
                .andExpect(jsonPath("$.customerName").value("customerName_01_up"))
                .andExpect(jsonPath("$.price").value(99));
    }

    /// ******************************************************************* ///
    /// *** DELETE method Entity: Ticket                                  *** ///
    /// ******************************************************************* ///
    @Test
    @Order(5)
    @DisplayName("DELETE-5 method: deleteTicket -> /api/tickets/{idTicket}")
    void deleteTicketTest() throws Exception {

        log.info("**************************************************************");
        log.info("* Testing DELETE method in Ticket entity (deleteTicket)      *");
        log.info("**************************************************************");

        mockMvc.perform(delete("/api/tickets/{idTicket}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
    }
}
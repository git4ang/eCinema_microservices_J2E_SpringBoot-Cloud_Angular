package ang.neggaw.cities;

import ang.neggaw.cities.entities.City;
import ang.neggaw.cities.services.CityService;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Log4j2
class M10_CityMicroserviceAppTests {

    @Autowired
    private CityService cityService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    /// **************************************************************** ///
    /// *** POST method in Entity: City                             *** ///
    /// **************************************************************** ///
    @Test
    @Order(1)
    @DisplayName(value = "POST:1 (createCity) => /api/cities")
    void createCityTest() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing POST method in City entity (createCity)     *");
        log.info("*******************************************************");

        City city = new City("city_01", 2.93, 43.12, 7);
        city.setIdCinema(1);

        mockMvc.perform(post("/api/cities")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(city)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idCity").exists())
                .andExpect(jsonPath("$.name", is("city_01")))
                .andExpect(jsonPath("$.idCinema", is(0)))
                .andExpect(jsonPath("$.cinemas").doesNotExist());
    }


    /// **************************************************************** ///
    /// *** GET method in Entity: City                              *** ///
    /// **************************************************************** ///
    @Test
    @Order(2)
    @DisplayName(value = "GET:2 (getAllCities) => /api/cities")
    void getAllCitiesTest() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing GET method in City entity (getAllCities)    *");
        log.info("*******************************************************");

        Stream.of(
                new City("city_02", 2, 43, 7),
                new City("city_03", 2, 43, 7),
                new City("city_04", 2, 43, 7)
        ).forEach(v -> cityService.createCity(v));

        mockMvc.perform(get("/api/cities")
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[*].idCity").isNotEmpty())
                .andExpect(jsonPath("$[2].entityState", is("CREATED")))
                .andExpect(jsonPath("$[4].entityState", is("CREATED")))
                .andExpect(jsonPath("$.size()", is(6)));
    }


    /// **************************************************************** ///
    /// *** GET method in Entity: City                              *** ///
    /// **************************************************************** ///
    @Test
    @Order(3)
    @DisplayName(value = "GET:3 (getCityById) => /api/cities/{idCity}")
    void getCityByIdTest() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing GET method in City entity (getCityById)     *");
        log.info("*******************************************************");

        City city = cityService.getCity(3, false);

        mockMvc.perform(get("/api/cities/{idCity}", city.getIdCity()))
                .andDo(print())
                .andExpect(jsonPath("$.idCity").exists())
                .andExpect(jsonPath("name", is("city_01")))
                .andExpect(status().isOk());

        // others verification
        assertNotNull(city);
        assertEquals("city_01", city.getName());
        assertEquals(0, city.getIdCinema());
        assertNull(city.getCinema());
    }


    /// **************************************************************** ///
    /// *** PUT method in Entity: City                              *** ///
    /// **************************************************************** ///
    @Test
    @Order(4)
    @DisplayName(value = "PUT:4 (updateCity) => /api/cities/{idCity}")
    void updateCityTest() throws Exception {

        log.info("*******************************************************");
        log.info("* Testing PUT method in City entity (updateCity)      *");
        log.info("*******************************************************");

        City cityDB_Update = new City("city_02_Up", 2,10,7);

        mockMvc.perform(put("/api/cities/{id}", 4)
                .content(objectMapper.writeValueAsString(cityDB_Update))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("city_02_Up")))
                .andExpect(jsonPath("$.entityState", is("UPDATED")));
    }


    /// ****************************************************************** ///
    /// *** DELETE method in Entity: City                             *** ///
    /// ****************************************************************** ///
    @Test
    @Order(7)
    @DisplayName(value = "DELETE:7 (deleteCity) => /api/cities/{idCity}")
    void deleteCityTest() throws Exception {

        log.info("**********************************************************");
        log.info("* Testing DELETE method in City entity (deleteCity)      *");
        log.info("**********************************************************");

        mockMvc.perform(delete("/api/cities/{idCity}", 3)
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
    }
}
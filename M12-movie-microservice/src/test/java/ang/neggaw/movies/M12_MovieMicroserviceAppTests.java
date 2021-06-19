package ang.neggaw.movies;

import ang.neggaw.movies.entities.Category;
import ang.neggaw.movies.entities.Movie;
import ang.neggaw.movies.entities.MovieProjection;
import ang.neggaw.movies.entities.MovieSession;
import ang.neggaw.movies.services.CategoryService;
import ang.neggaw.movies.services.MovieService;
import ang.neggaw.movies.services.ProjectionService;
import ang.neggaw.movies.services.SessionService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;
import java.util.stream.Stream;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/////////////////////////////////////////////////////////////////////////////////////////////////
///       The project 'M10-city-microservice' must be started for everything to work          ///
///       The project 'M11-cinema-microservice' must be started for everything to work        ///
/////////////////////////////////////////////////////////////////////////////////////////////////

@Log4j2
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@AutoConfigureMockMvc
@SpringBootTest
class M12_MovieMicroserviceAppTests {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private MovieService movieService;

    @Autowired
    private ProjectionService projectionService;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;


    /// ******************************************************************* ///
    /// *** POST method Entities: Category, Movie, Projection & MovieSession   *** ///
    /// ******************************************************************* ///
    @Test
    @Order(1)
    @DisplayName("POST-1 method: createCategory -> /api/categories")
    void createCategoryTest() throws Exception {

        log.info("***********************************************************");
        log.info("* Testing POST method in Category entity (createCategory) *");
        log.info("***********************************************************");

        Category category = new Category("thriller");
        mockMvc.perform(post("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(category)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Order(2)
    @DisplayName("POST-2 method: createMovie -> /api/movies")
    void createMovieTest() throws Exception {

        log.info("***********************************************************");
        log.info("* Testing POST method in Movie entity (createMovie)         *");
        log.info("***********************************************************");

        Movie movie = new Movie(
                "movieTitle_01",
                90,
                "directorName",
                "Some description about this movie...",
                "photoMovie.jpeg",
                1);

        mockMvc.perform(post("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movie)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Order(3)
    @DisplayName("POST-3 method: createProjection -> /api/projections")
    void createProjectionTest() throws Exception {

        log.info("***********************************************************************");
        log.info("* Testing POST method in MovieProjection entity (createProjection) *");
        log.info("***********************************************************************");

        MovieProjection projMovie = new MovieProjection(new Date(), 15, 2, 1);
        mockMvc.perform(post("/api/projections")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projMovie)))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @Test
    @Order(4)
    @DisplayName("POST-4 method: createSession -> /api/sessions")
    void createSessionTest() throws Exception {

        log.info("***********************************************************");
        log.info("* Testing POST method in MovieSession entity (createSession)     *");
        log.info("***********************************************************");

        MovieSession session = new MovieSession(new Date(), 1);
        mockMvc.perform(post("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(session)))
                .andDo(print())
                .andExpect(status().isCreated());
    }


    /// ******************************************************************* ///
    /// *** GET method Entities: Category, Movie, Projection & MovieSession   *** ///
    /// ******************************************************************* ///
    @Test
    @Order(5)
    @DisplayName("GET-5 method: getAllCategories -> /api/categories")
    void getAllCategoriesTest() throws Exception {

        log.info("************************************************************");
        log.info("* Testing GET method in Category entity (getAllCategories) *");
        log.info("************************************************************");

        Stream.of(
                new Category("action"),
                new Category("comedy"),
                new Category("karate")
        ).forEach(c -> categoryService.createCategory(c));

        mockMvc.perform(get("/api/categories")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[*].idCategory").isNotEmpty())
                .andExpect(jsonPath("$.size()", is(5)));
    }

    @Test
    @Order(6)
    @DisplayName("GET-6 method: getAllMovies -> /api/movies")
    void getAllMoviesTest() throws Exception {

        log.info("***********************************************************");
        log.info("* Testing GET method in Movie entity (getAllMovies)         *");
        log.info("***********************************************************");

        Stream.of(
                new Movie("movieTitle_02", 120, "directorName 02",
                        "Some description about this movie...", "photoMovie_02.jpeg", 2),

                new Movie("movieTitle_03", 100, "directorName 03",
                        "Some description about this movie...", "photoMovie_03.jpeg", 2),

                new Movie("movieTitle_04", 90, "directorName 01",
                        "Some description about this movie...", "photoMovie_01.jpeg", 1)
        ).forEach(movie -> movieService.createMovie(movie));

        mockMvc.perform(get("/api/movies")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[*].idMovie").isNotEmpty())
                .andExpect(jsonPath("$.size()").value(6));
    }

    @Test
    @Order(7)
    @DisplayName("GET-7 method: getAllProjections -> /api/projections")
    void getAllProjectionsTest() throws Exception {

        log.info("***********************************************************************");
        log.info("* Testing GET method in MovieProjection entity (getAllProjections) *");
        log.info("***********************************************************************");

        Stream.of(
                new MovieProjection(new Date(), 15, 3, 2),
                new MovieProjection(new Date(), 20, 4, 1),
                new MovieProjection(new Date(), 25, 5, 2)
        ).forEach(projectionMovie -> projectionService.createProjection(projectionMovie));

        mockMvc.perform(get("/api/projections")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[*].idProjection").isNotEmpty())
                .andExpect(jsonPath("$.size()", is(6)));
    }

    @Test
    @Order(8)
    @DisplayName("GET-8 method: getAllSessions -> /api/sessions")
    void getAllSessionsTest() throws Exception {

        log.info("***********************************************************");
        log.info("* Testing GET method in MovieSession entity (getAllSessions)     *");
        log.info("***********************************************************");

        Stream.of(
                new MovieSession(new Date(), 3),
                new MovieSession(new Date(), 4),
                new MovieSession(new Date(), 5)
        ).forEach(session -> sessionService.createSession(session));

        mockMvc.perform(get("/api/sessions")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").exists())
                .andExpect(jsonPath("$[*].idSession").isNotEmpty())
                .andExpect(jsonPath("$.size()").value(4));
    }


    /// ******************************************************************* ///
    /// *** GET method Entities: Category, Movie, Projection & MovieSession   *** ///
    /// ******************************************************************* ///
    @Test
    @Order(9)
    @DisplayName("GET-9 method: getCategoryById -> /api/categories/{idCat}")
    void getCategoryByIdTest() throws Exception {

        log.info("***********************************************************");
        log.info("* Testing GET method in Category entity (getCategoryById) *");
        log.info("***********************************************************");

        mockMvc.perform(get("/api/categories/{idCat}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("suspense"))
                .andExpect(jsonPath("$.entityState").value("CREATED"));
    }

    @Test
    @Order(10)
    @DisplayName("GET-10 method: getMovieById -> /api/movies/{idMovie}")
    void getMovieByIdTest() throws Exception {

        log.info("***********************************************************");
        log.info("* Testing GET method in Movie entity (getMovieById)         *");
        log.info("***********************************************************");

        mockMvc.perform(get("/api/movies/{idMovie}", 3)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("movieTitle_01"))
                .andExpect(jsonPath("$.entityState").value("CREATED"));
    }

    @Test
    @Order(11)
    @DisplayName("GET-11 method: getProjectionById -> /api/projections/{idProj}")
    void getProjectionByIdTest() throws Exception {

        log.info("***********************************************************************");
        log.info("* Testing GET method in MovieProjection entity (getProjectionById) *");
        log.info("***********************************************************************");

        mockMvc.perform(get("/api/projections/{idProj}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idMovie").value(1))
                .andExpect(jsonPath("$.entityState").value("CREATED"));
    }

    @Test
    @Order(12)
    @DisplayName("GET-12 method: getSessionById -> /api/sessions/{idSession}")
    void getSessionByIdTest() throws Exception {

        log.info("***********************************************************");
        log.info("* Testing GET method in MovieSession entity (getSessionById)     *");
        log.info("***********************************************************");

        mockMvc.perform(get("/api/sessions/{idSession}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.idProjection").value(1))
                .andExpect(jsonPath("$.entityState").value("CREATED"));
    }

    /// ******************************************************************* ///
    /// *** PUT method Entities: Category, Movie, Projection & MovieSession   *** ///
    /// ******************************************************************* ///
    @Test
    @Order(13)
    @DisplayName("PUT-13 method: updateCategory -> /api/categories/{idCat}")
    void updateCategoryTest() throws Exception {

        log.info("***********************************************************");
        log.info("* Testing PUT method in Category entity (updateCategory)  *");
        log.info("***********************************************************");

        Category categoryUpdate = new Category("History_up");
        mockMvc.perform(put("/api/categories/{idCat}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryUpdate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entityState").value("UPDATED"))
                .andExpect(jsonPath("$.name").value("History_up"));
    }

    @Test
    @Order(14)
    @DisplayName("PUT-14 method: updateMovie -> /api/movies/{idMovie}")
    void updateMovieTest() throws Exception {

        log.info("***********************************************************");
        log.info("* Testing PUT method in Movie entity (updateMovie)          *");
        log.info("***********************************************************");

        Movie movieUpdate = new Movie("movieTitle_up", 100, "directorName",
                "bla bla bla about description", "photoMovie.png", 1);
        mockMvc.perform(put("/api/movies/{idMovie}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(movieUpdate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entityState").value("UPDATED"))
                .andExpect(jsonPath("$.title").value("movieTitle_up"));
    }

    @Test
    @Order(15)
    @DisplayName("PUT-15 method: updateProjection -> /api/projections/{idProj}")
    void updateProjectionTest() throws Exception {

        log.info("**********************************************************************");
        log.info("* Testing PUT method in MovieProjection entity (updateProjection) *");
        log.info("**********************************************************************");

        MovieProjection projectionMovieUpdate = new MovieProjection(new Date(), 17, 2, 2);
        mockMvc.perform(put("/api/projections/{idProj}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(projectionMovieUpdate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entityState").value("UPDATED"))
                .andExpect(jsonPath("$.idMovie").value(2))
                .andExpect(jsonPath("$.idRoom").value(2));
    }

    @Test
    @Order(16)
    @DisplayName("PUT-16 method: updateSession -> /api/sessions/{idSession}")
    void updateSessionTest() throws Exception {

        log.info("***********************************************************");
        log.info("* Testing PUT method in MovieSession entity (updateSession)      *");
        log.info("***********************************************************");

        MovieSession sessionUpdate = new MovieSession(new Date(), 2);
        mockMvc.perform(put("/api/sessions/{idSession}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(sessionUpdate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.entityState").value("UPDATED"));
        //.andExpect(jsonPath("$.idProjectionMovie").value(1));
    }


    /// ******************************************************************* ///
    /// *** DELETE method Entities: Category, Movie, Projection & MovieSession   *** ///
    /// ******************************************************************* ///
    @Test
    @Order(17)
    @DisplayName("DELETE-17 method: deleteCategory -> /api/categories/{idCat}")
    void deleteCategoryTest() throws Exception {

        log.info("*************************************************************");
        log.info("* Testing DELETE method in Category entity (deleteCategory) *");
        log.info("*************************************************************");

        mockMvc.perform(delete("/api/categories/{idCat}", 4)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    @Order(18)
    @DisplayName("DELETE-18 method: deleteMovie -> /api/movies/{idMovie}")
    void deleteMovieTest() throws Exception {

        log.info("***********************************************************");
        log.info("* Testing DELETE method in Movie entity (deleteMovie)     *");
        log.info("***********************************************************");

        mockMvc.perform(delete("/api/movies/{idMovie}", 1)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    @Order(19)
    @DisplayName("DELETE-19 method: deleteProjection -> /api/projections/{idProj}")
    void deleteProjectionTest() throws Exception {

        log.info("*************************************************************************");
        log.info("* Testing DELETE method in MovieProjection entity (deleteProjection)    *");
        log.info("*************************************************************************");

        mockMvc.perform(delete("/api/projections/{idProj}", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
    }

    @Test
    @Order(20)
    @DisplayName("DELETE-20 method: deleteSession -> /api/sessions/{idSession}")
    void deleteSessionTest() throws Exception {

        log.info("***********************************************************");
        log.info("* Testing DELETE method in MovieSession entity (deleteSession)   *");
        log.info("***********************************************************");

        mockMvc.perform(delete("/api/sessions/{idSession}", 2)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isAccepted());
    }
}

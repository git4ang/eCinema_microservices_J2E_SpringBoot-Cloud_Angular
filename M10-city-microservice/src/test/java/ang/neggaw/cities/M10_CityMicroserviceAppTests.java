package ang.neggaw.cities;

import lombok.extern.log4j.Log4j2;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@AutoConfigureMockMvc
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Log4j2
class M10_CityMicroserviceAppTests {

    @Test
    void test_toDo_later() {
        assertEquals(17, 7 + 10);
    }
}
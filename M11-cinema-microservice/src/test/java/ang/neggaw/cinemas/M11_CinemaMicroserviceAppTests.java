package ang.neggaw.cinemas;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class M11_CinemaMicroserviceAppTests {

    @Test
    void test_toDo_later() {
        assertEquals("ok", 17, 7 + 10);
    }
}

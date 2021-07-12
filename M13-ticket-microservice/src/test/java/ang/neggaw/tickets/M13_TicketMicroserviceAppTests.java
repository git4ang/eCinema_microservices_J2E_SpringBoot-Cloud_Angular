package ang.neggaw.tickets;

import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;

import static org.springframework.test.util.AssertionErrors.assertEquals;

@SpringBootTest
class M13_TicketMicroserviceAppTests {

    @Test
    void test_toDo_later() {
        assertEquals("ok", 17, 7 + 10);
    }
}
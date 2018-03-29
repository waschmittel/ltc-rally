package de.flubba.rally;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = { RallyApplication.class,
                            ExceptionConfiguration.class,
                            RallyConfiguration.class })
@AutoConfigureTestDatabase
public class RallyApplicationTests {

    @Test
    public void contextLoads() {
    }

}

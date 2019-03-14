package de.flubba.rally;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

// Jsr310JpaConverters needed to properly persist java.time.*
@EntityScan
@SpringBootApplication
public class RallyApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(RallyApplication.class, args);
    }
}

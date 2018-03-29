package de.flubba.rally;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;

// Jsr310JpaConverters needed to properly persist java.time.*
@EntityScan(basePackageClasses = { RallyApplication.class, Jsr310JpaConverters.class })
@SpringBootApplication
public class RallyApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(RallyApplication.class, args);
    }

    // needed for WAR
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(RallyApplication.class);
    }
}

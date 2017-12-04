package es.rvp.web.vws.resources.controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot Application for spring boot testing support
 *
 * @author Rodrigo Villamil Perez
 */
@SpringBootApplication
public class TestSupport {
    /**
     * Main method for spring boot applications
     * @param args argument list
     */
    public static void main(final String[] args) {
        SpringApplication.run(TestSupport.class, args);
    }
}


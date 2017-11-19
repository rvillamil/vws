package es.rvp.web.vws.domain;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Spring boot Application for integration testing support
 *
 * @author Rodrigo Villamil Perez
 */
@SpringBootApplication
public class IntegrationTestSupport {
	/**
	 * Main method for spring boot applications
	 * @param args argument list
	 */
	public static void main(final String[] args) {
		SpringApplication.run(IntegrationTestSupport.class, args);
	}
}


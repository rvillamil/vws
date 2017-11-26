package es.rvp.web.vws;


import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Contexto para los test. El anteriormente conocido como application-context
 * para test
 *
 * @Configuration anotation: "Tags the class as a source of bean definitions for
 * the application context."
 *
 * @author Rodrigo Villamil Perez
 */
@Configuration
@ComponentScan("es.rvp.web.vws")
public class TestConfig {
}

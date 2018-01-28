package es.rvp.web.vws.resources.controllers;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

// TODO: Auto-generated Javadoc
/**
 * Spring boot Application for spring boot testing support.
 *
 * @author Rodrigo Villamil Perez
 */
@SpringBootApplication
public class TestSupport {
    
    /**
     * Main method for spring boot applications.
     *
     * @param args argument list
     */
    public static void main(final String[] args) {
        SpringApplication.run(TestSupport.class, args);
    }

    /**
     * To json.
     *
     * @param obj the obj
     * @return the byte[]
     * @throws JsonProcessingException the json processing exception
     */
    public static byte[] toJson(Object obj) throws JsonProcessingException {
    	final ObjectMapper map = new ObjectMapper();
    	return map.writeValueAsString(obj).getBytes();
    }
}



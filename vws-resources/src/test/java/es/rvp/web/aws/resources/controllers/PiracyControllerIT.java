package es.rvp.web.aws.resources.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Set;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author Rodrigo Villamil Perez
 */
@RunWith(SpringRunner.class)
/*
 * The webEnvironment attribute allows specific “web environments” to be
 * configured for the test. You can start tests with a MOCK servlet environment
 * or with a real HTTP server running on either a RANDOM_PORT or a DEFINED_PORT.
 *
 * https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4
 *
 */
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
@Ignore
public class PiracyControllerIT {


	@Autowired
	private TestRestTemplate restTemplate;

	//@Autowired
	//private WebTorrentSpider webTorrentSpider;

	//--------------------------- parseBillboardFilms -------------------------
	@Test
	@Ignore
	public void whenParseOneFilmInBillBoardThenGetOneFilmInBillBoard() {
		// When
		// TODO 07: Montar todos los servicios REST con sus test unitarios y de integracion. Estamos teniendo problemas
		// para deserializar. ¿Tenemos que implementar un deserializador?
		@SuppressWarnings("rawtypes")
		final ResponseEntity<Set> response = this.restTemplate.getForEntity("/hackbillboardfilms", Set.class);
		final Set<?> showsJSONFormat = response.getBody();
		final HashMap<?, ?> hashMap = (HashMap<?, ?>) showsJSONFormat.iterator().next();

		// Then
		assertNotNull(response);
		assertEquals(hashMap.get("title"), "testFilm_0");
		assertTrue(response.getStatusCode() == HttpStatus.OK);
	}
	//--------------------------- parseVideoPremieres -------------------------
	//---------------------------- parseTVShow --------------------------------

}

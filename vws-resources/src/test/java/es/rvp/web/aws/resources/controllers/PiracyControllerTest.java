package es.rvp.web.aws.resources.controllers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import es.rvp.web.vws.domain.Show;
import es.rvp.web.vws.resources.controllers.PiracyController;
import es.rvp.web.vws.services.WebTorrentSpider;

/**
 * @author Rodrigo Villamil Perez
 */
public class PiracyControllerTest {

	// Clase a testear
	private PiracyController piracyController;

	// Clases a mockear
	private WebTorrentSpider webTorrentSpider;

	@Before
	public void setup() {
		webTorrentSpider = mock(WebTorrentSpider.class);
		piracyController = new PiracyController(webTorrentSpider);
		piracyController.maxBillboardFilms  = 2;
		piracyController.maxVideoPremieres  = 3;
		piracyController.maxTVshows 		= 3;
	}

	//--------------------------- parseBillboardFilms -------------------------
	@Test
	public void whenParseFilmsInBillBoardThenGetTwoFilmsInBillBoard() {
		// Given
		// When
		when(webTorrentSpider.parseBillboardFilms(piracyController.maxBillboardFilms)).thenReturn(
				newShowsToTest(2, "La Isla"));

		final Set<Show> filmsBillboard = piracyController.parseBillBoardFilms();
		// Then
		assertNotNull(filmsBillboard);
		assertEquals(filmsBillboard, newShowsToTest(2, "La Isla"));
		assertTrue(filmsBillboard.size() == 2);
	}

	//--------------------------- parseVideoPremieres -------------------------
	@Test
	public void whenParseVideoPremieresThenGetTwoVideoPremieres() {
		// Given
		// When
		when(webTorrentSpider.parseVideoPremieres(piracyController.maxVideoPremieres)).thenReturn(
				newShowsToTest(2, "La Isla en Video"));

		final Set<Show> filmsBillboard = piracyController.parseVideoPremieres();
		// Then
		assertNotNull(filmsBillboard);
		assertEquals(filmsBillboard, newShowsToTest(2, "La Isla en Video"));
		assertTrue(filmsBillboard.size() == 2);
	}
	//---------------------------- parseTVShow --------------------------------
	@Test
	public void whenParseTVShowThenGetTheLastThreeEpisodes() {
		// Given
		String tvShowPath="series-hd/modern-family/";

		// When
		when(webTorrentSpider.parseTVShow(tvShowPath,piracyController.maxTVshows)).thenReturn(
						newShowsToTest(2, "Modern Family HD"));

		final Set<Show> filmsBillboard = piracyController.parseTVShow(tvShowPath);
		// Then
		assertNotNull(filmsBillboard);
		assertEquals(filmsBillboard, newShowsToTest(2, "Modern Family HD"));
		assertTrue(filmsBillboard.size() == 2);

	}


	//-------------------------- Helpers Methods ------------------------------
	private Set<Show> newShowsToTest(final int numShows, final String prefixName) {
		final Set<Show> shows = new LinkedHashSet<>();

		for (int i = 0; i < numShows; i++) {
			shows.add(Show.builder().title(prefixName + "_" + i).build());
		}

		return shows;
	}
}

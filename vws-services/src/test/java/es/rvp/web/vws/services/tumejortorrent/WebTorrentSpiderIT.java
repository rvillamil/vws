/**
 *
 */
package es.rvp.web.vws.services.tumejortorrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import es.rvp.web.vws.TestConfig;
import es.rvp.web.vws.domain.Show;
import es.rvp.web.vws.services.WebTorrentSpider;

/*
 * @author Rodrigo Villamil Perez
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestConfig.class)
public class WebTorrentSpiderIT {

	// Interface a testear
	@Autowired
	private WebTorrentSpider 	webTorrentSpider;

	//--------------------------- parseHTMLFrom -------------------------
	@Test
	public void givenURLWithFilmWhenParseGetTheShowObject() {
		// Given
		final String urlWithShow = "http://tumejortorrent.com/descargar-pelicula/monster-trucks/ts-screener/";
		// When
		final Show show = webTorrentSpider.parseHTMLFrom(urlWithShow);
		// Then
		assertNotNull 	( show );

		assertEquals 	( show.getBaseURI(), urlWithShow );
		assertNotNull 	( show.getTitle());
		assertNull 		( show.getSession());
		assertNull 		( show.getEpisode());
		assertNotNull 	( show.getQuality());
		assertNotNull 	( show.getFileSize());
		assertNull 		( show.getFilmaffinityPoints());
		assertNotNull 	( show.getReleaseDate());
		assertNotNull 	( show.getURLTODownlad() );
		assertNotNull 	( show.getURLWithCover());
		assertNotNull 	( show.getDescription());
		assertNotNull 	( show.getSinopsis());
	}

	@Test
	public void givenURLWithTVShowWhenParseGetTheShowObject() {
		// Given
		final String urlWithShow = "http://tumejortorrent.com/descargar-seriehd/the-big-bang-theory/capitulo-1018/hdtv-720p-ac3-5-1/";
		// When
		final Show show = webTorrentSpider.parseHTMLFrom(urlWithShow);
		// Then
		assertNotNull 	( show );

		assertEquals 	( show.getBaseURI(), urlWithShow );
		assertNotNull 	( show.getTitle());
		assertNotNull 	( show.getSession());
		assertNotNull 	( show.getEpisode());
		assertNotNull 	( show.getQuality());
		assertNotNull 	( show.getFileSize());
		assertNull 		( show.getFilmaffinityPoints());
		assertNotNull 	( show.getReleaseDate());
		assertNotNull 	( show.getURLTODownlad() );
		assertNotNull 	( show.getURLWithCover());
		assertNotNull 	( show.getDescription());
		assertNotNull 	( show.getSinopsis());
	}

	@Test
	public void givenExistingURLWithNoShowWhenParseGetNull() {
		// Given
		// Ojo, la pagina existe y devuelve contenido
		final String urlWithShow = "http://tumejortorrent.com/invented/";
		// When
		final Show show = webTorrentSpider.parseHTMLFrom(urlWithShow);
		// Then
		assertNotNull 	( show );
		assertEquals 	( show.getBaseURI(), urlWithShow );
		assertNull 		( show.getURLTODownlad() );
	}

	@Test
	public void givenNotDomainsURLWithNoShowWhenParseGetNull() {
		// Given
		final String urlWithShow = "http://sfgdgdopguregjer0"; // No pertenece al dominio de tumejortorrent.com
		// When
		final Show show = webTorrentSpider.parseHTMLFrom(urlWithShow);
		// Then
		assertNull 	( show );
	}

	//--------------------------- parseBillboardFilms -------------------------
	@Test
	public void whenParseBillBoardWithMoreFiveFilmsThenGetFiveFilms() {
		// Given
		// When
		final Set<Show> shows = webTorrentSpider.parseBillboardFilms(5);
		// Then
		assertNotNull 	( shows );
		assertEquals 	( shows.size(), 5);
	}

	@Test
	public void whenParseBillBoardWithThwoHundredFilmsGetAllShows() {
		// Given
		// When
		final Set<Show> shows = webTorrentSpider.parseBillboardFilms(200);
		// Then
		assertNotNull 	( shows );
		assertTrue	( shows.size() < 200);
	}

	//--------------------------- parseVideoPremieres -------------------------
	@Test
	public void whenParseVideoPremieresWithMoreFiveFilmsThenGetFiveFilms() {
		// Given
		// When
		final Set<Show> shows = webTorrentSpider.parseVideoPremieres(5);
		// Then
		assertNotNull 	( shows );
		assertEquals 	( shows.size(), 5);
	}

	@Test
	public void whenParseVideoPremieresWithThwoHundredFilmsGetAllShows() {
		// Given
		// When
		final Set<Show> shows = webTorrentSpider.parseVideoPremieres(200);
		// Then
		assertNotNull 	( shows );
		assertTrue	( shows.size() < 200);
	}

	//--------------------------- parseTVShow ---------------------------------
	@Test
	public void whenParseModernFamilyThenGetTheLastThreeEpisodes() {
		// Given
		// When
		final Set<Show> shows = webTorrentSpider.parseTVShow("/series-hd/modern-family", 3);
		// Then
		assertNotNull 	( shows );
		assertEquals 	( shows.size(), 3);
	}

	@Test
	public void whenParseModernFamilyWithThwoHundredThenGetTheLastEpisodes() {
		// Given
		// When
		final Set<Show> shows = webTorrentSpider.parseTVShow("/series-hd/modern-family", 200);
		// Then
		assertNotNull 	( shows );
		assertTrue 	( shows.size() < 200);
	}
}

package es.rvp.web.vws.domain.tumejortorrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import es.rvp.web.vws.TestConfig;
import es.rvp.web.vws.components.jsoup.JSoupHelper;
import es.rvp.web.vws.domain.ShowFieldParser;
import es.rvp.web.vws.utils.HTMLFactorySingleton;

// TODO: Auto-generated Javadoc
/**
 * The Class ShowEpisodeParserIT.
 */
/*
 * @author Rodrigo Villamil Perez
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestConfig.class)
public class ShowEpisodeParserIT {

	/** The jsoup helper. */
	// Clase de apoyo
	@Autowired
	private JSoupHelper jsoupHelper;

	/** The show episode parser. */
	// Interface a testear
	@Autowired
	private ShowFieldParser showEpisodeParser;

	/**
	 * Given HTML with TV show when parse then get episode not null.
	 */
	@Test
	public void givenHTMLWithTVShowWhenParseThenGetEpisodeNotNull() {
		// Given
		String html =HTMLFactorySingleton.INSTANCE.getHTMLByURL(this.jsoupHelper,
				"http://www.tumejortorrent.com/descargar-serie/mom/capitulo-418/hdtv/");
		// When
		String data = this.showEpisodeParser.parse(html);
		// Then
		assertNotNull 	( data );
		assertEquals 	( data, "18");
	}

	/**
	 * Given HTML with TV show with 2 episodes when parse then get two episodes.
	 */
	@Test
	public void givenHTMLWithTVShowWith2EpisodesWhenParseThenGetTwoEpisodes() {
		// Given
		// Esta seria devuelve dos episodios
		String html =HTMLFactorySingleton.INSTANCE.getHTMLByURL(this.jsoupHelper,
				"http://www.tumejortorrent.com/descargar-serie/the-man-in-the-high-castle/capitulo-25/hdtv/");
		// When
		String data = this.showEpisodeParser.parse(html);
		// Then
		assertNotNull 	( data );
		assertEquals 	( data, "5&6");
	}

	/**
	 * Given HTML with film when parse then get episode null.
	 */
	@Test
	public void givenHTMLWithFilmWhenParseThenGetEpisodeNull() {
		// Given
		String html =HTMLFactorySingleton.INSTANCE.getHTMLByURL(this.jsoupHelper,
				"http://tumejortorrent.com/descargar-pelicula/monster-trucks/ts-screener/");
		// When
		String data = this.showEpisodeParser.parse(html);
		// Then
		assertNull 	( data );
	}

}

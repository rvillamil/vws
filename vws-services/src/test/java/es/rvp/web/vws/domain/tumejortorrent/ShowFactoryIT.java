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
import es.rvp.web.vws.domain.Show;
import es.rvp.web.vws.domain.ShowFactory;

/**
 * The Class ShowFactoryIT.
 */
/*
 * @author Rodrigo Villamil Perez
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestConfig.class)
public class ShowFactoryIT {

	/** The jsoup helper. */
	// Clase de apoyo
	@Autowired
	private JSoupHelper jsoupHelper;

	/** The show factory. */
	// Interface a testear
	@Autowired
	private ShowFactory showFactory;

	/**
	 * Given HTML with film when parse then get show instance.
	 */
	// ------------------------ newInstance -----------------------------------
	@Test
	public void givenHTMLWithFilmWhenParseThenGetShowInstance() {
		// Given
		final String urlWithShow = "http://tumejortorrent.com/descargar-pelicula/monster-trucks/ts-screener/";

		// When
		final Show show = this.showFactory.newInstance( urlWithShow,
												  this.jsoupHelper.newInstanceByURL(urlWithShow).html());

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
		assertNotNull 	( show.getURLTODownload());
		assertNotNull 	( show.getURLWithCover());
		assertNotNull 	( show.getDescription());
		assertNotNull 	( show.getSinopsis());
	}

	/**
	 * Given HTML with TV show when parse then get show instance.
	 */
	@Test
	public void givenHTMLWithTVShowWhenParseThenGetShowInstance() {

		// Given
		final String urlWithShow = "http://www.tumejortorrent.com/descargar-serie/the-man-in-the-high-castle/capitulo-25/hdtv/";
		// When
		final Show show = this.showFactory.newInstance( urlWithShow,
				  								 this.jsoupHelper.newInstanceByURL(urlWithShow).html());
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
		assertNotNull 	( show.getURLTODownload());
		assertNotNull 	( show.getURLWithCover());
		assertNotNull 	( show.getDescription());
		assertNotNull 	( show.getSinopsis());
	}
}

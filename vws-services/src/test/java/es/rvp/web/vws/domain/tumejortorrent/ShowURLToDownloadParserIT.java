package es.rvp.web.vws.domain.tumejortorrent;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.net.MalformedURLException;
import java.net.URL;

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
 * The Class ShowURLToDownloadParserIT.
 */
/*
 * @author Rodrigo Villamil Perez
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestConfig.class)
public class ShowURLToDownloadParserIT {

	/** The jsoup helper. */
	// Clase de apoyo
	@Autowired
	private JSoupHelper jsoupHelper;

	/** The show URL to download parser. */
	// Interface a testear
	@Autowired
	private ShowFieldParser showURLToDownloadParser;

	/**
	 * Given HTML with TV show when parse then get URL to download.
	 */
	@Test
	public void givenHTMLWithTVShowWhenParseThenGetURLToDownload() {
		// Given
		String html =HTMLFactorySingleton.INSTANCE.getHTMLByURL(this.jsoupHelper,
				"http://www.tumejortorrent.com/descargar-serie/mom/capitulo-418/hdtv/");
		// When
		String data = this.showURLToDownloadParser.parse(html);
		// Then
		assertNotNull 	( data );
		assertTrue (this.isURLValid (data));
	}

	/**
	 * Given HTML with film when parse then get URL to download.
	 */
	@Test
	public void givenHTMLWithFilmWhenParseThenGetURLToDownload() {
		// Given
		String html =HTMLFactorySingleton.INSTANCE.getHTMLByURL(this.jsoupHelper,
				"http://tumejortorrent.com/descargar-pelicula/monster-trucks/ts-screener/");
		// When
		String data = this.showURLToDownloadParser.parse(html);
		// Then
		assertNotNull 	( data );
		assertTrue (this.isURLValid (data));
	}

	/**
	 * Checks if is URL valid.
	 *
	 * @param urlString the url string
	 * @return true, if is URL valid
	 */
	// ----------------- Utilities -----------------
	private boolean isURLValid (final String urlString){
		Boolean valid=true;
		try {
			new URL (urlString);
		} catch (MalformedURLException e) {
			valid = false;
		}
		return valid;
	}
}

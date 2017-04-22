package es.rvp.web.vws.domain.tumejortorrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import es.rvp.web.vws.TestConfig;
import es.rvp.web.vws.components.jsoup.JSoupHelper;
import es.rvp.web.vws.domain.ShowFieldParser;
import es.rvp.web.vws.utils.HTMLFactorySingleton;

/*
 * @author Rodrigo Villamil Perez
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestConfig.class)
public class ShowQualityParserIT {

	// Clase de apoyo
	@Autowired
	private JSoupHelper jsoupHelper;

	// Interface a testear
	@Autowired
	private ShowFieldParser showQualityParser;

	@Test
	public void givenHTMLWithTVShowWhenParseThenGetEpisodeNotNull() {
		// Given
		String html =HTMLFactorySingleton.INSTANCE.getHTMLByURL(this.jsoupHelper,
				"http://www.tumejortorrent.com/descargar-serie/mom/capitulo-418/hdtv/");
		// When
		String data = this.showQualityParser.parse(html);
		// Then
		assertNotNull 	( data );
		assertEquals 	( data, "HDTV");
	}


	@Test
	@Ignore
	// TODO 08-b: Como sabemos que es una cadena qeu corresfonde con la calidad?
	public void givenHTMLWithFilmWhenParseThenGetEpisodeNull() {
		// Given
		String html =HTMLFactorySingleton.INSTANCE.getHTMLByURL(this.jsoupHelper,
				"http://tumejortorrent.com/descargar-pelicula/monster-trucks/ts-screener/");
		// When
		String data = this.showQualityParser.parse(html);
		// Then
		assertNull 	( data );
	}
}

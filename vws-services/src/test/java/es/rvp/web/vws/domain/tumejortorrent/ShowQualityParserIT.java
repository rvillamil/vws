package es.rvp.web.vws.domain.tumejortorrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import es.rvp.web.vws.TestConfig;
import es.rvp.web.vws.components.jsoup.JSoupHelper;
import es.rvp.web.vws.domain.ShowFieldParser;
import es.rvp.web.vws.utils.HTMLFactorySingleton;

/**
 * The Class ShowQualityParserIT.
 */
/*
 * @author Rodrigo Villamil Perez
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestConfig.class)
public class ShowQualityParserIT {

	/** The jsoup helper. */
	// Clase de apoyo
	@Autowired
	private JSoupHelper jsoupHelper;

	/** The show quality parser. */
	// Interface a testear
	@Autowired
	private ShowFieldParser showQualityParser;

	/**
	 * Given HTML with TV show when parse then get episode not null.
	 */
	@Test
	public void givenHTMLWithTVShowWhenParseThenGetEpisodeNotNull() {
		// Given
		final String html =HTMLFactorySingleton.INSTANCE.getHTMLByURL(this.jsoupHelper,
				"http://www.tumejortorrent.com/descargar-serie/mom/capitulo-418/hdtv/");
		// When
		final String data = this.showQualityParser.parse(html);
		// Then
		assertNotNull 	( data );
		assertEquals 	( data, "HDTV");
	}
}

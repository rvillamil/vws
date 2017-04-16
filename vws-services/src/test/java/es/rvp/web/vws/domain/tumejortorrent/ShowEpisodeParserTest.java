package es.rvp.web.vws.domain.tumejortorrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import es.rvp.web.vws.components.jsoup.JSoupHelper;
import es.rvp.web.vws.components.jsoup.JSoupHelperImpl;
import es.rvp.web.vws.domain.ShowFieldParser;

/**
 * @author Rodrigo Villamil Perez
 */
public class ShowEpisodeParserTest {

	// Clases a testear
	private ShowFieldParser 		showSessionParser;

	// Clases a mockear
	private JSoupHelper 			jSoupHelper;

	@Before
	public void setup() {
		jSoupHelper 		= mock (JSoupHelperImpl.class);
		showSessionParser  	= new ShowSessionParser (jSoupHelper);
	}

	// -------------------------- parse -----------------------------------
	@Test
	public void givenHTMLWithSessionFieldParseThenGetTheSessionString() {

		// Given
		String htmlFragment = "loquesea HTML";

		// When
		when (jSoupHelper.selectElementText (
				anyObject(),
				anyString(),
				anyInt()) ).thenReturn(
						"Modern Family - Temporada 8 [HDTV 720p][Cap.809][AC3 5.1 Español Castellano]");
		assertEquals("8", showSessionParser.parse(htmlFragment));
	}

	@Test
	public void givenHTMLWithOutSessionFieldParseThenGetNull() {

		// Given
		String htmlFragment = "loquesea HTML";

		// When
		when (jSoupHelper.selectElementText (
				anyObject(),
				anyString(),
				anyInt()) ).thenReturn(
						"Modern Family - Temporada 8 [HDTV 720p][AC3 5.1 Español Castellano]");

		assertNull(showSessionParser.parse(htmlFragment));
	}
}

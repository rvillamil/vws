/**
 *
 */
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
public class ShowQualityParserTest {

	// Clases a testear
	private ShowFieldParser 		showQualityParser;

	// Clases a mockear
	private JSoupHelper 			jSoupHelper;

	@Before
	public void setup() {
		jSoupHelper 		= mock (JSoupHelperImpl.class);
		showQualityParser  	= new ShowQualityParser (jSoupHelper);
	}

	// -------------------------- parse -----------------------------------
	@Test
	public void givenHTMLWithQualityFieldParseThenGetTheQualityString() {

		// Given
		String htmlFragment = "loquesea HTML";

		// When
		when (jSoupHelper.selectElementText (
				anyObject(),
				anyString(),
				anyInt()) ).thenReturn(
						"Modern Family - Temporada 8 [HDTV 720p][Cap.809][AC3 5.1 Español Castellano]");
		String data = showQualityParser.parse(htmlFragment);
		// Then
		assertEquals ("HDTV 720p", data);
	}

	@Test
	public void givenHTMLWithOutQualityFieldParseThenGetNull() {

		// Given
		String htmlFragment = "loquesea HTML";

		// When
		when (jSoupHelper.selectElementText (
				anyObject(),
				anyString(),
				anyInt()) ).thenReturn(
						"Modern Family - Temporada 8 [Cap.809][AC3 5.1 Español Castellano]");

		String data = showQualityParser.parse(htmlFragment);

		// Then
		assertNull (data);
	}
}

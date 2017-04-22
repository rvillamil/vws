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
public class ShowSessionParserTest {

	// Clases a testear
	private ShowFieldParser 		showSessionParser;

	// Clases a mockear
	private JSoupHelper 			jSoupHelper;

	@Before
	public void setup() {
		this.jSoupHelper 		= mock (JSoupHelperImpl.class);
		this.showSessionParser  	= new ShowSessionParser (this.jSoupHelper);
	}

	@Test
	public void givenHTMLWithSessionFieldParseThenGetTheSessionString() {

		// Given
		String htmlFragment = "loquesea HTML";

		// When
		when (this.jSoupHelper.selectElementText (
				anyObject(),
				anyString(),
				anyInt()) ).thenReturn(
						"Mom  /  Mom - Temporada 4 [HDTV][Cap.418][AC3 5.1 Español Castellano]");
		String data = this.showSessionParser.parse(htmlFragment);
		// Then
		assertEquals ("4", data);
	}

	@Test
	public void givenHTMLWithTVShowWithTwoEpisodesWhenParseThenGetTheSession() {

		// Given
		String htmlFragment = "loquesea HTML";

		// When
		when (this.jSoupHelper.selectElementText (
				anyObject(),
				anyString(),
				anyInt()) ).thenReturn(
						"The Man in the High Castle  /  The Man in the High Castle - Temporada 2 [HDTV][Cap.205_206][Español Castellano]");
		assertEquals("2", this.showSessionParser.parse(htmlFragment));
	}

	@Test
	public void givenHTMLWithOutSessionFieldParseThenGetNull() {

		// Given
		String htmlFragment = "loquesea HTML";

		// When
		when (this.jSoupHelper.selectElementText (
				anyObject(),
				anyString(),
				anyInt()) ).thenReturn(
						"Modern Family - Temporada 8 [HDTV 720p][AC3 5.1 Español Castellano]");

		String data = this.showSessionParser.parse(htmlFragment);

		// Then
		assertNull (data);
	}
}

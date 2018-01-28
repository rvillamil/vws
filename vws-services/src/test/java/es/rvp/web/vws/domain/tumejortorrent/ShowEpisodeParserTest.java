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

// TODO: Auto-generated Javadoc
/**
 * The Class ShowEpisodeParserTest.
 *
 * @author Rodrigo Villamil Perez
 */
public class ShowEpisodeParserTest {

	/** The show episode parser. */
	// Clases a testear
	private ShowFieldParser 		showEpisodeParser;

	/** The j soup helper. */
	// Clases a mockear
	private JSoupHelper 			jSoupHelper;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		this.jSoupHelper 		= mock (JSoupHelperImpl.class);
		this.showEpisodeParser  	= new ShowEpisodeParser (this.jSoupHelper);
	}

	/**
	 * Given HTML with TV show when parse then get the episode.
	 */
	@Test
	public void givenHTMLWithTVShowWhenParseThenGetTheEpisode() {

		// Given
		String htmlFragment = "loquesea HTML";

		// When
		when (this.jSoupHelper.selectElementText (
				anyObject(),
				anyString(),
				anyInt()) ).thenReturn(
						"Mom  /  Mom - Temporada 4 [HDTV][Cap.418][AC3 5.1 Español Castellano]");
		assertEquals("18", this.showEpisodeParser.parse(htmlFragment));
	}

	/**
	 * Given HTML with TV show with two episodes when parse then get the episodes.
	 */
	@Test
	public void givenHTMLWithTVShowWithTwoEpisodesWhenParseThenGetTheEpisodes() {

		// Given
		String htmlFragment = "loquesea HTML";

		// When
		when (this.jSoupHelper.selectElementText (
				anyObject(),
				anyString(),
				anyInt()) ).thenReturn(
						"The Man in the High Castle  /  The Man in the High Castle - Temporada 2 [HDTV][Cap.205_206][Español Castellano]");
		assertEquals("5&6", this.showEpisodeParser.parse(htmlFragment));
	}


	/**
	 * Given HTML with out session field parse then get null.
	 */
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

		assertNull(this.showEpisodeParser.parse(htmlFragment));
	}
}

package es.rvp.web.vws.domain.tumejortorrent;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import es.rvp.web.vws.domain.ShowFieldParser;

// TODO: Auto-generated Javadoc
/**
 * The Class ShowURLToDownloadParserTest.
 *
 * @author Rodrigo Villamil Perez
 */
public class ShowURLToDownloadParserTest {

	/** The show URL to download parser. */
	// Clases a testear
	private ShowFieldParser 		showURLToDownloadParser;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		this.showURLToDownloadParser  = new ShowURLToDownloadParser ();
	}

	/**
	 * Given HTML with URL to download field parse then get the URL to download string.
	 */
	@Test
	@Ignore // Usar otro tipo de test ¿Un Spy?
	public void givenHTMLWithURLToDownloadFieldParseThenGetTheURLToDownloadString() {
		// Given
		String htmlFragment = "loquesea HTML";
		// When
		ShowFieldParser spyShowFieldParser = Mockito.spy(this.showURLToDownloadParser);
		spyShowFieldParser.parse(htmlFragment);

		String data = this.showURLToDownloadParser.parse(htmlFragment);

		// Then
		assertEquals (data, "http://wwww.urltodownload.com");
	}

	/**
	 * Given HTML with out URL to download field parse then get null.
	 */
	@Test
	@Ignore
	public void givenHTMLWithOutURLToDownloadFieldParseThenGetNull() {
	}
}

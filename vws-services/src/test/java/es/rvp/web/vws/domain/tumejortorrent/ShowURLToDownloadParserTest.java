/**
 *
 */
package es.rvp.web.vws.domain.tumejortorrent;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.mockito.Mockito;

import es.rvp.web.vws.domain.ShowFieldParser;

/**
 * @author Rodrigo Villamil Perez
 */
public class ShowURLToDownloadParserTest {

	// Clases a testear
	private ShowFieldParser 		showURLToDownloadParser;

	@Before
	public void setup() {
		this.showURLToDownloadParser  = new ShowURLToDownloadParser ();
	}

	@Test
	@Ignore
	public void givenHTMLWithURLToDownloadFieldParseThenGetTheURLToDownloadString() {
		// Given
		String htmlFragment = "loquesea HTML";
		// When
		// TODO : Usar otro tipo de test ¿Un Spy?
		ShowFieldParser spyShowFieldParser = Mockito.spy(this.showURLToDownloadParser);
		spyShowFieldParser.parse(htmlFragment);

		String data = this.showURLToDownloadParser.parse(htmlFragment);

		// Then
		assertEquals (data, "http://wwww.urltodownload.com");
	}

	@Test
	@Ignore
	public void givenHTMLWithOutURLToDownloadFieldParseThenGetNull() {
	}
}

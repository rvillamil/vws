package es.rvp.web.vws.services.tumejortorrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Matchers.anyObject;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import es.rvp.web.vws.components.jsoup.JSoupHelper;
import es.rvp.web.vws.components.jsoup.JSoupHelperImpl;
import es.rvp.web.vws.domain.Show;
import es.rvp.web.vws.domain.ShowFactory;
import es.rvp.web.vws.domain.tumejortorrent.ShowFactoryImpl;
import es.rvp.web.vws.services.WebTorrentSpider;

/*
 * @author Rodrigo Villamil Perez
 */
public class WebTorrentSpiderTest {

	// Interface a testear
	private WebTorrentSpider 		webTorrentSpider;

	// Clases a mockear
	private JSoupHelper 			jSoupHelper;
	private ShowFactory				showFactory;

	@Before
	public void setup() {
		this.jSoupHelper 		= mock ( JSoupHelperImpl.class);
		this.showFactory  		= mock ( ShowFactoryImpl.class);

		this.webTorrentSpider   = new WebTorrentSpiderImpl (this.jSoupHelper,
															this.showFactory);
	}

	//--------------------------- parseHTMLFrom -------------------------------
	@Test
	public void givenNotDomainsURLWithNoShowWhenParseGetNull() {
		// Given
		final String urlWithShow = "http://www.invalidvalidurlwithshow.com";

		// When
		Document doc = new Document (urlWithShow);
		when (this.jSoupHelper.newInstanceByURL(urlWithShow)).thenReturn(doc);
		when (this.showFactory.newInstance(urlWithShow, doc.html())).thenReturn(Show.builder().title("test").build());

		Show show = this.webTorrentSpider.parseHTMLFrom(urlWithShow);
		// Then
		assertNull ( show);
	}

	@Test
	public void givenExitingURLBelongsToTuMejorTorrentWithShowWhenParseGetTheShowObject() {
		// Given
		final String urlWithShow = "http://tumejortorrent.com/anyvalidurlwithshow.com";

		// When
		Document doc = new Document (urlWithShow);
		when (this.jSoupHelper.newInstanceByURL(urlWithShow)).thenReturn(doc);
		when (this.showFactory.newInstance(urlWithShow, doc.html())).thenReturn(
				Show.builder().title("test").build());

		Show show = this.webTorrentSpider.parseHTMLFrom(urlWithShow);
		// Then
		assertNotNull ( show);
		assertEquals  ( show.getTitle(), "test");
	}

	@Test
	public void givenNotExitingURLBelongsToTuMejorTorrentWithShowWhenParseGetTheShowObject() {
		// Given
		final String urlWithShow = "http://tumejortorrent.com/notexisting.com";

		// When
		Document doc = new Document (urlWithShow);
		when (this.jSoupHelper.newInstanceByURL(urlWithShow)).thenReturn(null); // No existe la URL
		when (this.showFactory.newInstance(urlWithShow, doc.html())).thenReturn(
				Show.builder().title("test").build());

		Show show = this.webTorrentSpider.parseHTMLFrom(urlWithShow);
		// Then
		assertNull ( show);
	}

	//--------------------------- parseBillboardFilms -------------------------
	@Test
	public void whenParseBillBoardWithMoreFiveFilmsThenGetFiveFilms() {
		// Given
		int numberOfFilmsInBillBoard = 20;

		final String urlWithShow = "http://tumejortorrent.com";
		Show.builder().title("test").build();
		// When
		Document doc = new Document (urlWithShow);
		when (this.jSoupHelper.newInstanceByURL(anyString())).thenReturn(doc);
		when (this.jSoupHelper.selectElementsByClassListName(doc,"pelilist")).thenReturn(this.getTestElements(numberOfFilmsInBillBoard));
		when (this.jSoupHelper.newInstanceFromElementWithURL(anyObject())).thenReturn(new Document ("showhtml")); // Crea un elemento con el show

		// Hacemos este artificio, para que no meta elementos repetidos
		when (this.showFactory.newInstance(anyString(), anyString())).thenAnswer(invocation -> {
			Thread.sleep(10);
			String title = String.valueOf(System.currentTimeMillis());
			return Show.builder().title(title).build();
		});

		final Set<Show> shows = this.webTorrentSpider.parseBillboardFilms(5);
		// Then
		assertNotNull 	( shows );
		assertEquals 	( shows.size(), 5);
	}

	// TODO 08-c: Test Unitarios - Completar resto de test ...

	// ----------------------- test utils -------------------------------------
	private Elements getTestElements (final int numberOfElements){
		Elements elements = new Elements();

		for (int i=0; i < numberOfElements; i++) {
			Element e = new Element (Tag.valueOf("tag"), "element_"+ i+1);
			elements.add(e);
		}

		return elements;
	}

	//--------------------------- parseVideoPremieres -------------------------
	//--------------------------- parseTVShow ---------------------------------

}
;
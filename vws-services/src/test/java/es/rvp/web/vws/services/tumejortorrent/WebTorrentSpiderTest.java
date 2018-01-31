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

/**
 * The Class WebTorrentSpiderTest.
 */
/*
 * @author Rodrigo Villamil Perez
 */
public class WebTorrentSpiderTest {

	/** The web torrent spider. */
	// Interface a testear
	private WebTorrentSpider 		webTorrentSpider;

	/** The j soup helper. */
	// Clases a mockear
	private JSoupHelper 			jSoupHelper;
	
	/** The show factory. */
	private ShowFactory				showFactory;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		this.jSoupHelper 		= mock ( JSoupHelperImpl.class);
		this.showFactory  		= mock ( ShowFactoryImpl.class);
		this.webTorrentSpider   = new WebTorrentSpiderImpl (this.jSoupHelper,
				this.showFactory);
	}

	/**
	 * Given not domains URL with no show when parse get null.
	 */
	//--------------------------- parseHTMLFrom -------------------------------
	@Test
	public void givenNotDomainsURLWithNoShowWhenParseGetNull() {
		// Given
		final String urlWithShow = "http://www.invalidvalidurlwithshow.com";

		// When
		final Document doc = new Document (urlWithShow);
		when (this.jSoupHelper.newInstanceByURL(urlWithShow)).thenReturn(doc);
		when (this.showFactory.newInstance(urlWithShow, doc.html())).thenReturn(Show.builder().title("test").build());

		final Show show = this.webTorrentSpider.parseHTMLFrom(urlWithShow);
		// Then
		assertNull ( show);
	}

	/**
	 * Given exiting URL belongs to tu mejor torrent with show when parse get the show object.
	 */
	@Test
	public void givenExitingURLBelongsToTuMejorTorrentWithShowWhenParseGetTheShowObject() {
		// Given
		final String urlWithShow = "http://tumejortorrent.com/anyvalidurlwithshow.com";

		// When
		final Document doc = new Document (urlWithShow);
		when (this.jSoupHelper.newInstanceByURL(urlWithShow)).thenReturn(doc);
		when (this.showFactory.newInstance(urlWithShow, doc.html())).thenReturn(
				Show.builder().title("test").build());

		final Show show = this.webTorrentSpider.parseHTMLFrom(urlWithShow);
		// Then
		assertNotNull ( show);
		assertEquals  ( show.getTitle(), "test");
	}

	/**
	 * Given not exiting URL belongs to tu mejor torrent with show when parse get the show object.
	 */
	@Test
	public void givenNotExitingURLBelongsToTuMejorTorrentWithShowWhenParseGetTheShowObject() {
		// Given
		final String urlWithShow = "http://tumejortorrent.com/notexisting.com";

		// When
		final Document doc = new Document (urlWithShow);
		when (this.jSoupHelper.newInstanceByURL(urlWithShow)).thenReturn(null); // No existe la URL
		when (this.showFactory.newInstance(urlWithShow, doc.html())).thenReturn(
				Show.builder().title("test").build());

		final Show show = this.webTorrentSpider.parseHTMLFrom(urlWithShow);
		// Then
		assertNull ( show);
	}

	/**
	 * When parse bill board with more five films then get five films.
	 */
	//--------------------------- parseBillboardFilms -------------------------
	@Test
	public void whenParseBillBoardWithMoreFiveFilmsThenGetFiveFilms() {
		// Given
		final int numberOfShowsInTheWebSite 	= 20;
		final int numberOfShowsToParse 		= 5;
		final String htmlClassName				= "pelilist";

		// When
		this.configureTest (numberOfShowsInTheWebSite,htmlClassName);
		final Set<Show> shows = this.webTorrentSpider.parseBillboardFilms(numberOfShowsToParse);
		// Then
		assertNotNull 	( shows );
		assertEquals 	( shows.size(), numberOfShowsToParse);
	}

	/**
	 * When parse twenty films in bill board with ten films then get ten films.
	 */
	@Test
	public void whenParseTwentyFilmsInBillBoardWithTenFilmsThenGetTenFilms() {
		// Given
		final int numberOfShowsInTheWebSite 	= 10;
		final int numberOfShowsToParse 		= 20;
		final String htmlClassName				= "pelilist";

		// When
		this.configureTest (numberOfShowsInTheWebSite,htmlClassName);
		final Set<Show> shows = this.webTorrentSpider.parseBillboardFilms(numberOfShowsToParse);
		// Then
		assertNotNull 	( shows );
		assertEquals 	( shows.size(), numberOfShowsInTheWebSite);
	}

	/**
	 * When parse video premieres with more five video then get five video.
	 */
	//--------------------------- parseVideoPremieres -------------------------
	@Test
	public void whenParseVideoPremieresWithMoreFiveVideoThenGetFiveVideo() {
		// Given
		final int numberOfShowsInTheWebSite 	= 20;
		final int numberOfShowsToParse 		= 5;
		final String htmlClassName				= "pelilist";

		// When
		this.configureTest (numberOfShowsInTheWebSite, htmlClassName);
		final Set<Show> shows = this.webTorrentSpider.parseVideoPremieres(numberOfShowsToParse);
		// Then
		assertNotNull 	( shows );
		assertEquals 	( shows.size(), numberOfShowsToParse);
	}

	/**
	 * When parse video premieres with more six video then get six video.
	 */
	@Test
	public void whenParseVideoPremieresWithMoreSixVideoThenGetSixVideo() {
		// Given
		final int numberOfShowsInTheWebSite 	= 20;
		final int numberOfShowsToParse 		= 6;
		final String htmlClassName				= "pelilist";

		// When
		this.configureTest (numberOfShowsInTheWebSite, htmlClassName);
		final Set<Show> shows = this.webTorrentSpider.parseVideoPremieres(numberOfShowsToParse);
		// Then
		assertNotNull 	( shows );
		assertEquals 	( shows.size(), numberOfShowsToParse);
	}

	/**
	 * When parse twenty video premieres in bill board with ten videos then get ten videos.
	 */
	@Test
	public void whenParseTwentyVideoPremieresInBillBoardWithTenVideosThenGetTenVideos() {
		// Given
		final int numberOfShowsInTheWebSite 	= 10;
		final int numberOfShowsToParse 		= 20;
		final String htmlClassName			= "pelilist";

		// When
		this.configureTest (numberOfShowsInTheWebSite/2,htmlClassName);
		final Set<Show> shows = this.webTorrentSpider.parseVideoPremieres(numberOfShowsToParse);
		// Then
		assertNotNull 	( shows );
		assertEquals 	( shows.size(), numberOfShowsInTheWebSite);
	}

	/**
	 * When parse modern family with 20 episodes then get the last three episodes.
	 */
	//--------------------------- parseTVShow ---------------------------------
	@Test
	public void whenParseModernFamilyWith20EpisodesThenGetTheLastThreeEpisodes() {
		// Given
		final int numberOfShowsInTheWebSite 	= 20;
		final int numberOfShowsToParse 		= 3;
		final String htmlClassName				= "buscar-list";

		// When
		this.configureTest (numberOfShowsInTheWebSite,htmlClassName);
		final Set<Show> shows = this.webTorrentSpider.parseTVShow("/series-hd/modern-family", numberOfShowsToParse);
		// Then
		assertNotNull 	( shows );
		assertEquals 	( shows.size(), numberOfShowsToParse);
	}

	/**
	 * When parse three modern family episode with thow episodes then get the two episodes.
	 */
	@Test
	public void whenParseThreeModernFamilyEpisodeWithThowEpisodesThenGetTheTwoEpisodes() {
		// Given
		final int numberOfShowsInTheWebSite 	= 2;
		final int numberOfShowsToParse 		= 3;
		final String htmlClassName			= "buscar-list";

		// When
		this.configureTest (numberOfShowsInTheWebSite,htmlClassName);
		final Set<Show> shows = this.webTorrentSpider.parseTVShow("/series-hd/modern-family", numberOfShowsToParse);
		// Then
		assertNotNull 	( shows );
		assertEquals 	( shows.size(), numberOfShowsInTheWebSite);
	}



	/**
	 * Gets the test elements.
	 *
	 * @param numberOfElements the number of elements
	 * @return the test elements
	 */
	// ----------------------- test utils -------------------------------------
	private Elements getTestElements (final int numberOfElements){
		final Elements elements = new Elements();

		for (int i=0; i < numberOfElements; ++i) {
			final Element e = new Element (Tag.valueOf("tag"), "element_"+ i+1);
			elements.add(e);
		}

		return elements;
	}

	/**
	 * Configure test.
	 *
	 * @param numberOfShowsInTheWebSite the number of shows in the web site
	 * @param classListName the class list name
	 */
	private void configureTest ( final int numberOfShowsInTheWebSite,
								 final String classListName){

		final String urlWithShow = "http://tumejortorrent.com";
		// When
		final Document doc = new Document (urlWithShow);
		when (this.jSoupHelper.newInstanceByURL(anyString())).thenReturn(doc);
		when (this.jSoupHelper.selectElementsByClassListName(doc, classListName)).thenReturn(this.getTestElements(numberOfShowsInTheWebSite));
		when (this.jSoupHelper.newInstanceFromElementWithURL(anyObject())).thenReturn(new Document ("showhtml")); // Crea un elemento con el show

		// Hacemos este artificio, para que no meta elementos repetidos
		when (this.showFactory.newInstance(anyString(), anyString())).thenAnswer(invocation -> {
			Thread.sleep(10);
			final String title = String.valueOf(System.currentTimeMillis());
			return Show.builder().title(title).build();
		});
	}
}
package es.rvp.web.vws.services.tumejortorrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import es.rvp.web.vws.TestConfig;
import es.rvp.web.vws.domain.Show;
import es.rvp.web.vws.services.WebTorrentSpider;
/**
 * The Class WebTorrentSpiderIT.
 */
/*
 * @author Rodrigo Villamil Perez
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestConfig.class)
public class WebTorrentSpiderIT {

    /** The web torrent spider. */
    // Interface a testear
    @Autowired
    private WebTorrentSpider 	webTorrentSpider;

    /**
     * Given URL with film when parse get the show object.
     */
    //--------------------------- parseHTMLFrom -------------------------
    @Test
    public void givenURLWithFilmWhenParseGetTheShowObject() {
        // Given
        final String urlWithShow = "http://tumejortorrent.com/descargar-pelicula/monster-trucks/ts-screener/";
        // When
        final Show show = this.webTorrentSpider.parseHTMLFrom(urlWithShow);
        // Then
        assertNotNull 	( show );

        assertEquals 	( show.getBaseURI(), urlWithShow );
        assertNotNull 	( show.getTitle());
        assertNull 		( show.getSession());
        assertNull 		( show.getEpisode());
        assertNotNull 	( show.getQuality());
        assertNotNull 	( show.getFileSize());
        assertNull 		( show.getFilmaffinityPoints());
        assertNotNull 	( show.getReleaseDate());
        assertNotNull 	( show.getURLTODownload() );
        assertNotNull 	( show.getURLWithCover());
        assertNotNull 	( show.getDescription());
        assertNotNull 	( show.getSinopsis());
    }

    /**
     * Given URL with TV show when parse get the show object.
     */
    @Test
    public void givenURLWithTVShowWhenParseGetTheShowObject() {
        // Given
        final String urlWithShow = "http://tumejortorrent.com/descargar-seriehd/the-big-bang-theory/capitulo-1018/hdtv-720p-ac3-5-1/";
        // When
        final Show show = this.webTorrentSpider.parseHTMLFrom(urlWithShow);
        // Then
        assertNotNull 	( show );

        assertEquals 	( show.getBaseURI(), urlWithShow );
        assertNotNull 	( show.getTitle());
        assertNotNull 	( show.getSession());
        assertNotNull 	( show.getEpisode());
        assertNotNull 	( show.getQuality());
        assertNotNull 	( show.getFileSize());
        assertNull 		( show.getFilmaffinityPoints());
        assertNotNull 	( show.getReleaseDate());
        assertNotNull 	( show.getURLTODownload() );
        assertNotNull 	( show.getURLWithCover());
        assertNotNull 	( show.getDescription());
        assertNotNull 	( show.getSinopsis());
    }

    /**
     * Given existing URL with no show when parse get null.
     */
    @Test
    public void givenExistingURLWithNoShowWhenParseGetNull() {
        // Given
        // Ojo, la pagina existe y devuelve contenido
        final String urlWithShow = "http://tumejortorrent.com/invented/";
        // When
        final Show show = this.webTorrentSpider.parseHTMLFrom(urlWithShow);
        // Then
        assertNotNull 	( show );
        assertEquals 	( show.getBaseURI(), urlWithShow );
        assertNull 		( show.getURLTODownload() );
    }

    /**
     * Given not domains URL with no show when parse get null.
     */
    @Test
    public void givenNotDomainsURLWithNoShowWhenParseGetNull() {
        // Given
        final String urlWithShow = "http://sfgdgdopguregjer0"; // No pertenece al dominio de tumejortorrent.com
        // When
        final Show show = this.webTorrentSpider.parseHTMLFrom(urlWithShow);
        // Then
        assertNull 	( show );
    }

    /**
     * When parse bill board with more five films then get five films.
     */
    //--------------------------- parseBillboardFilms -------------------------
    @Test
    public void whenParseBillBoardWithMoreFiveFilmsThenGetFiveFilms() {
        // Given
        // When
        final Set<Show> shows = this.webTorrentSpider.parseBillboardFilms(5);
        // Then
        assertNotNull 	( shows );
        assertEquals 	( shows.size(), 5);
    }


    /**
     * When parse video premieres with more five films then get five films.
     */
    //--------------------------- parseVideoPremieres -------------------------
    @Test
    public void whenParseVideoPremieresWithMoreFiveFilmsThenGetFiveFilms() {
        // Given
        // When
        final Set<Show> shows = this.webTorrentSpider.parseVideoPremieres(5);
        // Then
        assertNotNull 	( shows );

        assertTrue	( shows.size() <= 5);
    }


    /**
     * When parse modern family then get the last three episodes.
     */
    //----------------------------- parseTVShow -------------------------------
    @Test
    public void whenParseModernFamilyThenGetTheLastThreeEpisodes() {
        // Given
        // When
        final Set<Show> shows = this.webTorrentSpider.parseTVShow("modern-family", 3);
        // Then
        assertNotNull 	( shows );
        assertEquals 	( shows.size(), 3);
    }
}

package es.rvp.web.vws.domain.tumejortorrent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;

import es.rvp.web.vws.components.jsoup.JSoupHelper;
import es.rvp.web.vws.components.jsoup.JSoupHelperImpl;
import es.rvp.web.vws.domain.Show;
import es.rvp.web.vws.domain.ShowFactory;
import es.rvp.web.vws.domain.ShowFieldParser;

// TODO: Auto-generated Javadoc
/**
 * The Class ShowFactoryTest.
 *
 * @author Rodrigo Villamil Perez
 */
public class ShowFactoryTest {

    /** The show factory. */
    // Clases a testear
    private ShowFactory 				showFactory;

    /** The j soup helper. */
    // Clases a mockear
    private JSoupHelper 				jSoupHelper;
    
    /** The show episode parser. */
    private ShowFieldParser 			showEpisodeParser;
    
    /** The show quality parser. */
    private ShowFieldParser 			showQualityParser;
    
    /** The show session parser. */
    private ShowFieldParser 			showSessionParser;
    
    /** The show URL to download parser. */
    private ShowFieldParser 			showURLToDownloadParser;

    /**
     * Setup.
     */
    @Before
    public void setup() {
        this.jSoupHelper 			= mock (JSoupHelperImpl.class);
        this.showEpisodeParser		= mock (ShowEpisodeParser.class);
        this.showQualityParser		= mock (ShowQualityParser.class);
        this.showSessionParser		= mock (ShowSessionParser.class);
        this.showURLToDownloadParser = mock (ShowURLToDownloadParser.class);

        this.showFactory  		= new ShowFactoryImpl (
                this.jSoupHelper,
                this.showEpisodeParser,
                this.showQualityParser,
                this.showSessionParser,
                this.showURLToDownloadParser);
    }

    /**
     * Given HTML with TV show when parse then get show instance.
     */
    // ------------------------ newInstance -----------------------------------
    @Test
    public void givenHTMLWithTVShowWhenParseThenGetShowInstance() {
        // Given
        final String htmlFragment = "loquesea HTML";
        final Document doc = new Document (htmlFragment);

        // When
        when (this.jSoupHelper.newInstanceFromText(htmlFragment)).thenReturn(doc);
        // URLTODownload
        when (this.showURLToDownloadParser.parse(htmlFragment)).thenReturn("http://www.seriemolona.com");
        // -- Titulo
        when (this.jSoupHelper.selectElementText(doc, "strong", 1)).thenReturn("serieMolona");
        // -- Episodio
        when (this.showEpisodeParser.parse(htmlFragment)).thenReturn("9");
        // Sesion(temporada)
        when (this.showSessionParser.parse(htmlFragment)).thenReturn("8");
        // Calidad
        when (this.showQualityParser.parse(htmlFragment)).thenReturn("HDTV 720p");
        // Descripcion
        when (this.jSoupHelper.getElementTextByClass(doc, "descripcion_top", 0)).thenReturn("description molona");
        // Sinopsis
        when (this.jSoupHelper.getElementTextByClass(doc, "sinopsis", 0)).thenReturn("sinopsis molona");
        // Cover
        when (this.jSoupHelper.getElementURLIMGByClass (doc,"entry-left",0)).thenReturn("http://www.portadamolona.com");
        // FileSize
        when (this.jSoupHelper.getElementTextByClass(doc, "imp", 1)).thenReturn("567Mb");
        // ReleaseDate
        when (this.jSoupHelper.getElementTextByClass(doc, "imp", 2)).thenReturn("26/05/1976");

        final Show show = this.showFactory.newInstance("http://www.baseURI.com", htmlFragment);

        // Then
        assertEquals ("http://www.baseURI.com", show.getBaseURI());
        assertEquals ("http://www.seriemolona.com", show.getURLTODownload());
        assertEquals ("serieMolona", show.getTitle());
        assertEquals ("9", show.getEpisode());
        assertEquals ("8", show.getSession());
        assertEquals ("HDTV 720p", show.getQuality());
        assertNull   (show.getFilmaffinityPoints());
        assertEquals ("description molona", show.getDescription());
        assertEquals ("sinopsis molona", show.getSinopsis());
        assertEquals ("http://www.portadamolona.com", show.getURLWithCover());
        assertEquals ("567Mb", show.getFileSize());
        assertEquals ("26/05/1976", show.getReleaseDate());
    }

    /**
     * Given HTML with film when parse then get show instance.
     */
    @Test
    public void givenHTMLWithFilmWhenParseThenGetShowInstance() {

        // Given
        final String htmlFragment = "loquesea HTML";
        final Document doc = new Document (htmlFragment);

        // When
        when (this.jSoupHelper.newInstanceFromText(htmlFragment)).thenReturn(doc);
        // URLTODownload
        when (this.showURLToDownloadParser.parse(htmlFragment)).thenReturn("http://www.pelimolona.com");
        // -- Titulo
        when (this.jSoupHelper.selectElementText(doc, "strong", 1)).thenReturn("tituloMolon");
        // -- Episodio
        when (this.showEpisodeParser.parse(htmlFragment)).thenReturn(null);
        // Sesion(temporada)
        when (this.showSessionParser.parse(htmlFragment)).thenReturn(null);
        // Calidad
        when (this.showQualityParser.parse(htmlFragment)).thenReturn("HDTV 1080p");
        // Descripcion
        when (this.jSoupHelper.getElementTextByClass(doc, "descripcion_top", 0)).thenReturn("description molona");
        // Sinopsis
        when (this.jSoupHelper.getElementTextByClass(doc, "sinopsis", 0)).thenReturn("sinopsis molona");
        // Cover
        when (this.jSoupHelper.getElementURLIMGByClass (doc,"entry-left",0)).thenReturn("http://www.portadamolona.com");
        // FileSize
        when (this.jSoupHelper.getElementTextByClass(doc, "imp", 1)).thenReturn("567Mb");
        // ReleaseDate
        when (this.jSoupHelper.getElementTextByClass(doc, "imp", 2)).thenReturn("26/05/1976");

        final Show show = this.showFactory.newInstance("http://www.baseURI.com", htmlFragment);

        // Then
        assertEquals   	("http://www.baseURI.com", show.getBaseURI());
        assertEquals 	("tituloMolon", show.getTitle());
        assertNull 		(show.getSession());
        assertNull 		(show.getEpisode());
        assertEquals 	("HDTV 1080p", show.getQuality());
        assertNull   	(show.getFilmaffinityPoints());
        assertEquals 	("description molona", show.getDescription());
        assertEquals 	("sinopsis molona", show.getSinopsis());
        assertEquals 	("http://www.portadamolona.com", show.getURLWithCover());
        assertEquals 	("567Mb", show.getFileSize());
        assertEquals 	("26/05/1976", show.getReleaseDate());
    }
}

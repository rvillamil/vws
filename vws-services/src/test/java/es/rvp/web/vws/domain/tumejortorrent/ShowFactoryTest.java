/**
 *
 */
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

/**
 * @author Rodrigo Villamil Perez
 */
public class ShowFactoryTest {

	// Clases a testear
	private ShowFactory 				showFactory;

	// Clases a mockear
	private JSoupHelper 				jSoupHelper;
	private ShowFieldParser 			showEpisodeParser;
	private ShowFieldParser 			showQualityParser;
	private ShowFieldParser 			showSessionParser;
	private ShowFieldParser 			showURLToDownloadParser;

	@Before
	public void setup() {
		jSoupHelper 			= mock (JSoupHelperImpl.class);
		showEpisodeParser		= mock (ShowEpisodeParser.class);
		showQualityParser		= mock (ShowQualityParser.class);
		showSessionParser		= mock (ShowSessionParser.class);
		showURLToDownloadParser = mock (ShowURLToDownloadParser.class);

		showFactory  		= new ShowFactoryImpl (
				jSoupHelper,
				showEpisodeParser,
				showQualityParser,
				showSessionParser,
				showURLToDownloadParser);
	}

	// ------------------------ newInstance -----------------------------------
	@Test
	public void givenHTMLWithTVShowWhenParseThenGetShowInstance() {
		// Given
		String htmlFragment = "loquesea HTML";
		Document doc = new Document (htmlFragment);

		// When
		when (jSoupHelper.newInstanceFromText(htmlFragment)).thenReturn(doc);
		// URLTODownload
		when (showURLToDownloadParser.parse(htmlFragment)).thenReturn("http://www.seriemolona.com");
		// -- Titulo
		when (jSoupHelper.selectElementText(doc, "strong", 0)).thenReturn("serieMolona");
		// -- Episodio
		when (showEpisodeParser.parse(htmlFragment)).thenReturn("9");
		// Sesion(temporada)
		when (showSessionParser.parse(htmlFragment)).thenReturn("8");
		// Calidad
		when (showQualityParser.parse(htmlFragment)).thenReturn("HDTV 720p");
		// Descripcion
		when (jSoupHelper.getElementTextByClass(doc, "descripcion_top", 0)).thenReturn("description molona");
		// Sinopsis
		when (jSoupHelper.getElementTextByClass(doc, "sinopsis", 0)).thenReturn("sinopsis molona");
		// Cover
		when (jSoupHelper.getElementURLIMGByClass (doc,"entry-left",0)).thenReturn("http://www.portadamolona.com");
		// FileSize
		when (jSoupHelper.getElementTextByClass(doc, "imp", 1)).thenReturn("567Mb");
		// ReleaseDate
		when (jSoupHelper.getElementTextByClass(doc, "imp", 2)).thenReturn("26/05/1976");

		Show show = showFactory.newInstance("http://www.baseURI.com", htmlFragment);

		// Then
		assertEquals ("http://www.baseURI.com", show.getBaseURI());
		assertEquals ("http://www.seriemolona.com", show.getURLTODownlad());
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

	@Test
	public void givenHTMLWithFilmWhenParseThenGetShowInstance() {

		// Given
		String htmlFragment = "loquesea HTML";
		Document doc = new Document (htmlFragment);

		// When
		when (jSoupHelper.newInstanceFromText(htmlFragment)).thenReturn(doc);
		// URLTODownload
		when (showURLToDownloadParser.parse(htmlFragment)).thenReturn("http://www.pelimolona.com");
		// -- Titulo
		when (jSoupHelper.selectElementText(doc, "strong", 0)).thenReturn("tituloMolon");
		// -- Episodio
		when (showEpisodeParser.parse(htmlFragment)).thenReturn(null);
		// Sesion(temporada)
		when (showSessionParser.parse(htmlFragment)).thenReturn(null);
		// Calidad
		when (showQualityParser.parse(htmlFragment)).thenReturn("HDTV 1080p");
		// Descripcion
		when (jSoupHelper.getElementTextByClass(doc, "descripcion_top", 0)).thenReturn("description molona");
		// Sinopsis
		when (jSoupHelper.getElementTextByClass(doc, "sinopsis", 0)).thenReturn("sinopsis molona");
		// Cover
		when (jSoupHelper.getElementURLIMGByClass (doc,"entry-left",0)).thenReturn("http://www.portadamolona.com");
		// FileSize
		when (jSoupHelper.getElementTextByClass(doc, "imp", 1)).thenReturn("567Mb");
		// ReleaseDate
		when (jSoupHelper.getElementTextByClass(doc, "imp", 2)).thenReturn("26/05/1976");

		Show show = showFactory.newInstance("http://www.baseURI.com", htmlFragment);

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

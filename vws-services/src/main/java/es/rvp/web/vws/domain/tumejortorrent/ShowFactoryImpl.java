package es.rvp.web.vws.domain.tumejortorrent;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.rvp.web.vws.components.jsoup.JSoupHelper;
import es.rvp.web.vws.domain.Show;
import es.rvp.web.vws.domain.ShowFactory;
import es.rvp.web.vws.domain.ShowFieldParser;

/**
 * The Class ShowFactoryImpl.
 *
 * @author Rodrigo Villamil Perez
 */
@Component("showFactory")
public class ShowFactoryImpl implements ShowFactory {

	/** The Constant LOGGER. */
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger(ShowFactoryImpl.class);

	/** The j soup helper. */
	@Autowired
	private final JSoupHelper 				jSoupHelper;
	
	/** The show episode parser. */
	@Autowired
	private final ShowFieldParser 			showEpisodeParser;
	
	/** The show quality parser. */
	@Autowired
	private final ShowFieldParser 			showQualityParser;
	
	/** The show session parser. */
	@Autowired
	private final ShowFieldParser 			showSessionParser;
	
	/** The show URL to download parser. */
	@Autowired
	private final ShowFieldParser 			showURLToDownloadParser;


	/**
	 * Default builder.
	 *
	 * @param jSoupHelper The HTML parser helper
	 * @param showEpisodeParser the show episode parser
	 * @param showQualityParser the show quality parser
	 * @param showSessionParser the show session parser
	 * @param showURLToDownloadParse the show URL to download parse
	 */
	public ShowFactoryImpl(
			final JSoupHelper  		  	jSoupHelper,
			final ShowFieldParser 		showEpisodeParser,
			final ShowFieldParser		showQualityParser,
			final ShowFieldParser  		showSessionParser,
			final ShowFieldParser 		showURLToDownloadParse) {
		this.jSoupHelper			 = jSoupHelper;
		this.showEpisodeParser  	 = showEpisodeParser;
		this.showQualityParser		 = showQualityParser;
		this.showSessionParser 		 = showSessionParser;
		this.showURLToDownloadParser 	 = showURLToDownloadParse;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.rvp.web.vws.domain.ShowFactory#newInstance(java.lang.String)
	 */
	@Override
	public Show newInstance(final String baseURI, final String htmlDocument) {
		Show theShow = null;

		// Buscamos el boton de "descargar tu archivo torent"
		final Document doc 		= this.jSoupHelper.newInstanceFromText(htmlDocument);
		theShow = Show.builder().
				baseURI       ( baseURI).
				title		  ( this.jSoupHelper.selectElementText(doc, "strong", 1)).
				session 	  ( this.showSessionParser.parse(htmlDocument)).
				episode 	  ( this.showEpisodeParser.parse(htmlDocument)).
				description   ( this.jSoupHelper.getElementTextByClass(doc, "descripcion_top", 0)).
				sinopsis	  ( this.jSoupHelper.getElementTextByClass(doc, "sinopsis", 0)).
				quality		  ( this.showQualityParser.parse(htmlDocument) ).
				URLWithCover  ( this.jSoupHelper.getElementURLIMGByClass (doc,"entry-left",0)).
				fileSize	  ( this.jSoupHelper.getElementTextByClass(doc, "imp", 1)).
				releaseDate	  ( this.jSoupHelper.getElementTextByClass(doc, "imp", 2)).
				URLTODownload ( this.showURLToDownloadParser.parse(htmlDocument)).build();

		LOGGER.debug("newInstance: " + theShow.toString());

		return theShow;
	}
}

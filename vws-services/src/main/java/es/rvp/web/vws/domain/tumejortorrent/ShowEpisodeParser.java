package es.rvp.web.vws.domain.tumejortorrent;

import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.rvp.web.vws.components.jsoup.JSoupHelper;
import es.rvp.web.vws.domain.ShowFieldParser;

/**
 * Implements #{@link es.rvp.web.vws.domain.ShowFieldParser} for parsing the episode show field
 *
 * @author Rodrigo Villamil Perez
 */
@Component("showEpisodeParser")
public class ShowEpisodeParser implements ShowFieldParser {

	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger(ShowEpisodeParser.class);

	@Autowired
	private final JSoupHelper jSoupHelper;

	/**
	 * Builder
	 * @param jSoupHelper Facility to parse the HTML document
	 */
	public ShowEpisodeParser (final JSoupHelper jSoupHelper){
		super();
		this.jSoupHelper = jSoupHelper;
	}

	/* (non-Javadoc)
	 * @see es.rvp.web.vws.domain.ShowFieldParser#parse(java.lang.String)
	 */
	@Override
	public String parse(final String htmlDocument) {

		Document doc = new Document (htmlDocument);

		Integer episodeInt  = null;
		String  episode =  null;
		try {
			// Seleccionamos el encabezado de la pagina, e.g.:
			//		"Modern Family  /  Modern Family - Temporada 8 [HDTV 720p][Cap.809][AC3 5.1 Español Castellano]"
			episode = jSoupHelper.selectElementText (doc,"h1",0); // e.g. [TS Screener][Español Castellano][2017]
			String[] data = episode.split("Cap\\.");
			if (data.length > 1) {
				episode = data[1]; // --> Retorna algo del tipo  "811][AC3 5.1 Español Castellano]"
				episode = episode.substring(1,3); // --> Retorna 11
				episodeInt = Integer.parseInt(episode);
			}
		}
		catch (NumberFormatException ex) {
			LOGGER.warn("parsing field episode - is not number the string '" + episode + "'");
		}
		catch (Exception ex) {
			LOGGER.warn(ex.getMessage(), ex);
		}

		if (episodeInt!=null) {
			return episodeInt.toString();
		}else {
			return null;
		}
	}
}

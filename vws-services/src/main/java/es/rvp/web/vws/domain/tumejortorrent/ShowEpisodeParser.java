package es.rvp.web.vws.domain.tumejortorrent;

import org.jsoup.Jsoup;
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
		this.jSoupHelper = jSoupHelper;
	}

	/* (non-Javadoc)
	 * @see es.rvp.web.vws.domain.ShowFieldParser#parse(java.lang.String)
	 */
	@Override
	public String parse(final String htmlFragment) {
		String episodes  =  null;
		try {
			Document doc 	   = Jsoup.parseBodyFragment(htmlFragment);
			Integer oneEpisode =  null;
			Integer twoEpisode =  null;

			// Seleccionamos el encabezado de la pagina,
			// e.g.:
			//		"Mom  /  Mom - Temporada 4 [HDTV][Cap.418][AC3 5.1 Español Castellano]"
			// 	    "The Man in the High Castle  /  The Man in the High Castle - Temporada 2 [HDTV][Cap.205_206][Español Castellano]"
			//
			String  dataToParse = this.jSoupHelper.selectElementText (doc,"h1",0);
			String[] data = dataToParse.split("Cap\\.");
			if (data.length > 1) {
				dataToParse = data[1]; // --> Retorna algo del tipo  "418][AC3 5.1 Español Castellano]" o bien "205_206][Español Castellano]
				oneEpisode = Integer.parseInt(dataToParse.substring(1,3));
				// Son varios episodios ...
				if( dataToParse.contains("_") ) {
					twoEpisode =  Integer.parseInt( dataToParse.substring(5,7));
				} // --> Retorna 5&6
				episodes=oneEpisode.toString();
				if (twoEpisode!=null) {
					episodes += "&" + twoEpisode.toString();
				}
			}
		}
		catch (Exception ex) {
			LOGGER.warn(ex.getMessage(), ex);
		}

		return episodes;
	}
}

package es.rvp.web.vws.domain.tumejortorrent;

import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import es.rvp.web.vws.components.jsoup.JSoupHelper;
import es.rvp.web.vws.domain.ShowFieldParser;

/**
 * Implements #{@link es.rvp.web.vws.domain.ShowFieldParser} for parsing the quality show field
 *
 * @author Rodrigo Villamil Perez
 */
@Component("showQualityParser")
public class ShowQualityParser implements ShowFieldParser {

	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger(ShowQualityParser.class);

	@Autowired
	private final JSoupHelper jSoupHelper;

	/**
	 * Builder
	 * @param jSoupHelper Facility to parse the HTML document
	 */
	public ShowQualityParser (final JSoupHelper jSoupHelper){
		this.jSoupHelper = jSoupHelper;
	}

	/* (non-Javadoc)
	 * @see es.rvp.web.vws.domain.ShowFieldParser#parse(java.lang.String)
	 */
	@Override
	public String parse(final String htmlFragment) {
		Document doc = Jsoup.parseBodyFragment(htmlFragment);
		String quality  = null;
		try {
			quality = this.jSoupHelper.selectElementText (doc,"h1",0); // e.g. [TS Screener][Español Castellano][2017]
			quality = this.getTextBetweenBracketsByPosition(quality,1).trim();
		}
		catch (Exception ex) {
			LOGGER.warn(ex.getMessage(), ex);
		}

		return quality;
	}

	/**
	 * Get the text between the brackets of the position.
	 *
	 * @param text String with brackets
	 * 		e.g.: [one][two][three]hello[four]
	 * @param position The text between brackets position. Warning! Position [1..n]
	 * 	 	e.g: 2
	 * @return The text
	 * 		e.g: "two"
	 */
	private String getTextBetweenBracketsByPosition (final String text, final int position) {
		return text.split(Pattern.quote("["))[position].replace("]", "");
	}
}

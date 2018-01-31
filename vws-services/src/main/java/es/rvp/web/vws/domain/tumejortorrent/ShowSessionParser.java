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
 * Implements #{@link es.rvp.web.vws.domain.ShowFieldParser} for parsing the session show field
 *
 * @author Rodrigo Villamil Perez
 */
@Component("showSessionParser")
public class ShowSessionParser implements ShowFieldParser {

    /** The Constant LOGGER. */
    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowSessionParser.class);

    /** The j soup helper. */
    @Autowired
    private final JSoupHelper jSoupHelper;

    /**
     * Builder.
     *
     * @param jSoupHelper Facility to parse the HTML document
     */
    public ShowSessionParser (final JSoupHelper jSoupHelper){
        this.jSoupHelper = jSoupHelper;
    }

    /* (non-Javadoc)
     * @see es.rvp.web.vws.domain.ShowFieldParser#parse(java.lang.String)
     */
    @Override
    public String parse(final String htmlFragment) {
        final Document doc = Jsoup.parseBodyFragment(htmlFragment);
        Integer sessionInt  	= null;
        String session 			= null;
        try {
            // Seleccionamos el encabezado de la pagina, e.g.:
            //		"Modern Family  /  Modern Family - Temporada 8 [HDTV 720p][Cap.809][AC3 5.1 Español Castellano]"
            session = this.jSoupHelper.selectElementText (doc,"h1",1); // e.g. [TS Screener][Español Castellano][2017]
            final String[] data = session.split("Cap\\.");
            if (data.length > 1) {
                session = data[1]; // --> Retorna algo del tipo  "811][AC3 5.1 Español Castellano]"
                session = session.substring(0,1); // --> Retorna 8
                sessionInt = Integer.parseInt(session);
            }
        }
        catch (final NumberFormatException ex) {
            LOGGER.warn("parse session field - is not number the string '" + session + "'");
        }
        catch (final Exception ex) {
            LOGGER.warn(ex.getMessage(), ex);
        }

        if (sessionInt!=null) {
            return sessionInt.toString();
        }else {
            return null;
        }
    }
}

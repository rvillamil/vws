package es.rvp.web.vws.domain.tumejortorrent;

import java.util.Scanner;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import es.rvp.web.vws.domain.ShowFieldParser;

/**
 * Implements #{@link es.rvp.web.vws.domain.ShowFieldParser} for parsing the URLToDownload show field
 *
 * @author Rodrigo Villamil Perez
 */
@Component("showURLToDownloadParser")
public class ShowURLToDownloadParser implements ShowFieldParser {

    /** The Constant LOGGER. */
    // LOGGER
    private static final Logger LOGGER = LoggerFactory.getLogger(ShowURLToDownloadParser.class);

    /* (non-Javadoc)
     * @see es.rvp.web.vws.domain.ShowFieldParser#parse(java.lang.String)
     */
    @Override
    public String parse(final String htmlDocument) {
        String urlToDownLoad=null;

        final Scanner scanner = new Scanner(htmlDocument);
        while (scanner.hasNextLine()) {
            final String line = scanner.nextLine();
            // process the line
            if (line.contains("window.location.href")){
                final String[] lines = line.split("=");
                urlToDownLoad  = lines[1].replace("\"", "").replace(";", "").trim();
            }
        }
        scanner.close();

        if (urlToDownLoad==null || urlToDownLoad.isEmpty()) {
            LOGGER.warn("Problem parsing URLToDownload field!");
        }

        return urlToDownLoad;
    }
}

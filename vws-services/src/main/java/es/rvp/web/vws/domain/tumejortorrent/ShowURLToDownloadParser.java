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

	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger(ShowURLToDownloadParser.class);

	/* (non-Javadoc)
	 * @see es.rvp.web.vws.domain.ShowFieldParser#parse(java.lang.String)
	 */
	@Override
	public String parse(final String htmlDocument) {
		String urlToDownLoad=null;

		Scanner scanner = new Scanner(htmlDocument);
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			// process the line
			if (line.contains("window.location.href")){
				String[] lines = line.split("=");
				urlToDownLoad  = lines[1] + "=" + lines[2];
				urlToDownLoad  = urlToDownLoad.replace("\"", "");
				urlToDownLoad  = urlToDownLoad.replace(";", "").trim();
			}
		}
		scanner.close();

		if ((urlToDownLoad==null) || urlToDownLoad.isEmpty()) {
			LOGGER.warn("Problem parsing URLToDownload field!");
		}

		return urlToDownLoad;
	}
}

package es.rvp.web.vws.domain;

// TODO: Auto-generated Javadoc
/**
 * Interface for show field parser.
 * .e.g: Title, Session,...
 *
 * @author Rodrigo Villamil Perez
 */
public interface ShowFieldParser {

	/**
	 * Parse an HTML document, extracting one show field
	 * .e.g.: Session
	 *
	 * @param htmlDocument String with HTML content
	 * @return The value of field
	 */
	public String parse(String htmlDocument);
}

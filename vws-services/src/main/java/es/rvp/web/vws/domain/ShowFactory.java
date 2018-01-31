package es.rvp.web.vws.domain;

/**
 * Factory for #{@link es.rvp.web.vws.domain.Show} objects
 *
 * @author Rodrigo Villamil Perez
 */
public interface ShowFactory {

	/**
	 * Factory method. Create new #{@link es.rvp.web.vws.domain.Show} object by string with HTML
	 * content with show data
	 *
	 * @param baseURI Document URI
	 * @param htmlDocument String with HTML content
	 * @return The show object parsed, in the HTML document. Return null if there is not show
	 */
	Show newInstance(  String baseURI,  String htmlDocument );
}

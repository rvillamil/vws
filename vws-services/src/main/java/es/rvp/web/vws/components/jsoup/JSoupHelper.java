package es.rvp.web.vws.components.jsoup;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * Wrapper for JSoup library. Web scraping purposes
 *
 * Web scraping (web harvesting or web data extraction) is a computer software
 * technique of extracting information from websites. This is accomplished by
 * either directly implementing the Hypertext Transfer Protocol (on which the
 * Web is based), or embedding a web browser.
 *
 * @author Rodrigo Villamil Perez
 */
public interface JSoupHelper {

	/**
	 * Create HTML document by any text
	 *
	 * @param text String with HTML
	 * @return Jsoup Document object
	 */
	Document newInstanceFromText (String text);

	/**
	 * Create new jsoup document instance by existing URL
	 *
	 * @param urlString Existing/valid url
	 * @return Jsoup Document object or null
	 */
	Document newInstanceByURL(String urlString);

	/**
	 * Create new jsoup document instance with CONTENT of the URL in the first href string,
	 * from the element param
	 *
	 * @param element
	 *            jsoup element object with href subelement
	 *            .e.g: from tumejortorrent
	 *            <li>
	 *            <a href=
	 *            	"http://www.tumejortorrent.com/descargar-seriehd/modern-family/capitulo-810/hdtv-720p-ac3-5-1/"
	 *            	title="Serie en HD Modern Family Temporada 8 Capitulo 10">
	 *            	<img src="http://www.tumejortorrent.com/pictures/c/2261_modern-family.jpg"
	 *            	      alt="Serie en HD Modern Family Temporada 8 Capitulo 10"> </a>
	 *
	 *  				.....
	 *            </li>
	 *
	 * @return Jsoup Document object with the URL from element or null
	 * 		  .e.g: Get the document from the URL
	 *         href="http://www.tumejortorrent.com/descargar-seriehd/modern-family/capitulo-810/hdtv-720p-ac3-5-1/
	 *
	 */
	Document newInstanceFromElementWithURL(final Element element);

	/**
	 * Given document object with HTML List return the jsoup elemenst object whith <li> elements
	 *
	 * .e.g:
	 * <ul class="className">
	 * 		<li>one</li>
	 * 		<li>thow</li> ....
	 * 		<li>n</li>
	 * </ul>
	 *
	 * @param classWithNameOfList Class with name of the list
	 * 		 .e.g: "className"
	 *
	 * @param documen Jsoup document object with the HTML
	 *
	 * @return JSoup elements with the html list. If className not exists/not
	 *         found, the list size will be zero (empty list)
	 *      .e.g: elements = { [<li>one</li>], [<li>two</li>] ..etc }
	 */
	Elements selectElementsByClassListName(Document document, String classWithNameOfList);


	/**
	 * Find elements text, that match the Selector CSS query, with this element as the starting context.
	 *
	 * @param document The HTML document
	 * .e.g: <p>Hello</p>
	 * 		 <p>Hello2</p>
	 *
	 * @param cssQuery HTML element with TEXT. .e.g:. "p", "h1",
	 * .e.g: "p"
	 * @param index The index of the element, from the list of elements obtained
	 * .e.g: 1
	 * @return The text of the html element or null
	 * .e.g: "Hello2"
	 */
	String selectElementText(final Document document, final String cssQuery, final int index);


	/**
	 * Find elements that have this class, including or under this element. Case insensitive.
	 * Elements can have multiple classes (.e.g:. <div class="header round first">.
	 * This method checks each class, so you can find the above with el.getElementsByClass("header");.
	 *
	 * @param document The HTML document
	 * 	.e.g: <div class="claseTest">Hello</div>
	 * 		  <div class="claseTest">Hello2</div>
	 *
	 * @param className The class name
	 * .e.g: "claseTest"
	 * @param index The index of the element, from the list of elements obtained
	 * .e.g: 0
	 * @return The text of the html element or null
	 * .e.g: "Hello"
	 */
	String getElementTextByClass(final Document document, final String className, final int index);


	/**
	 * Find elements with url, that have this className
	 *
	 * @param document The HTML document
	 * 	.e.g:. <a href="http://www.google.es" title="Prueba" class="btn-torrent" target="_blank">Descarga tu Archivo torrent!</a>
	 *		   <a href="http://www.yahoo.es" title="Prueba2" class="btn-torrent" target="_blank">Descarga tu Archivo torrent!</a>
	 *
	 * @param className The class name
	 *  .e.g: "btn-torrent"
	 * @param index the index of the element, from the list of elements obtained
	 *  .e.g: 0
	 * @return The url text of the element or null
	 *  .e.g: "http://www.google.es"
	 */
	String getElementURLByClass(final Document document, final String className, final int index);


	/**
	 * Find the url in "src" from "img" elements, that have this className
	 *
	 * @param document The HTML document
	 * 	.e.g:.
	 * <div class="entry-left">
 	 *    <a href="series/modern-family/">
 	 *		<img src="http://www.prueba1.jpg"
 	 *		     alt="Descargar Modern Family - Temporada 8  torrent gratis" width="160"
 	 *		     height="230" style="margin:0px 10px;">
 	 *    </a>
 	 *	   <a href="series/modern-family/">
 	 *		<img src="http://www.prueba2.jpg">
 	 *	  </a>
 	 *  </div>
	 *
	 * @param className The class name
	 *  .e.g: "entry-left"
	 * @param index the index of the element, from the list of elements obtained
	 *  .e.g: 0
	 * @return The url text of the element or null
	 *  .e.g: "http://www.prueba1.jpg"
	 */
	String getElementURLIMGByClass(final Document document, final String className, final int index);
}
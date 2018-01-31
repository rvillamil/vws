package es.rvp.web.vws.utils;

import es.rvp.web.vws.components.jsoup.JSoupHelper;

/**
 * The Enum HTMLFactorySingleton.
 *
 * @author Rodrigo
 */
public enum HTMLFactorySingleton {
	
	/** The instance. */
	INSTANCE;

	/**
	 * New HTML fragment.
	 *
	 * @return the string
	 */
	public String newHTMLFragment () {
		final StringBuilder htmlFragment=new StringBuilder("<!DOCTYPE html>");
		htmlFragment.append("<html>");
		htmlFragment.append("<body>");
		htmlFragment.append("<h1>My First Heading</h1>");
		htmlFragment.append("<p>My first paragraph.</p>");
		htmlFragment.append("<p>My second paragraph.</p>");

		htmlFragment.append("</body>");
		htmlFragment.append("</html>");

		return htmlFragment.toString();
	}

	/**
	 * New HTML with H 1 header.
	 *
	 * @param h1header the h 1 header
	 * @return the string
	 */
	public String newHTMLWithH1Header(final String h1header) {
		final StringBuilder htmlFragment=new StringBuilder("<!DOCTYPE html>");
		htmlFragment.append("<html>");
		htmlFragment.append("<body>");
		htmlFragment.append("<h1>");
		htmlFragment.append(h1header);
		htmlFragment.append("</h1>");


		htmlFragment.append("<p>My first paragraph.</p>");
		htmlFragment.append("<p>My second paragraph.</p>");

		htmlFragment.append("</body>");
		htmlFragment.append("</html>");

		return htmlFragment.toString();
	}

	/**
	 * New HTML fragment with body.
	 *
	 * @param body the body
	 * @return the string
	 */
	public String newHTMLFragmentWithBody (final String body) {
		final StringBuilder htmlFragment=new StringBuilder("<!DOCTYPE html>");
		htmlFragment.append("<html>");
		htmlFragment.append("<body>");
		htmlFragment.append(body);
		htmlFragment.append("</body>");
		htmlFragment.append("</html>");

		return htmlFragment.toString();
	}

	/**
	 * New HTML with IMG fragment.
	 *
	 * @return the string
	 */
	public String newHTMLWithIMGFragment () {
		final StringBuilder htmlFragment=new StringBuilder("<!DOCTYPE html>");
		htmlFragment.append("<html>");
		htmlFragment.append("<body>");
		htmlFragment.append("<h1>My First Heading</h1>");
		htmlFragment.append("<p>My first paragraph.</p>");

		htmlFragment.append("<div class=\"entry-left\">");
		htmlFragment.append("<a href=\"series/modern-family/\">");
		htmlFragment.append("<img src=\"http://www.prueba1.jpg\"alt=\"Descargar 1\" width=\"160\">");
		htmlFragment.append("</a>");
		htmlFragment.append("<a href=\"series/modern-family/\">");
		htmlFragment.append("<img src=\"http://www.prueba2.jpg\"alt=\"Descargar 2\" width=\"180\">");
		htmlFragment.append("</a>");

		htmlFragment.append("</div>");

		htmlFragment.append("</body>");
		htmlFragment.append("</html>");

		return htmlFragment.toString();
	}


	/**
	 * New HTML with class fragment.
	 *
	 * @return the string
	 */
	public String newHTMLWithClassFragment () {
		final StringBuilder htmlFragment=new StringBuilder("<!DOCTYPE html>");
		htmlFragment.append("<html>");
		htmlFragment.append("<body>");
		htmlFragment.append("<h1>My First Heading</h1>");
		htmlFragment.append("<p>My first paragraph.</p>");
		htmlFragment.append("<div class=\"claseTest\">Hello</div>");
		htmlFragment.append("<div class=\"claseTest\">Hello2</div>");

		htmlFragment.append("</body>");
		htmlFragment.append("</html>");

		return htmlFragment.toString();
	}

	/**
	 * New HTML with URL class fragment.
	 *
	 * @return the string
	 */
	public String newHTMLWithURLClassFragment () {
		final StringBuilder htmlFragment=new StringBuilder("<!DOCTYPE html>");
		htmlFragment.append("<html>");
		htmlFragment.append("<body>");
		htmlFragment.append("<h1>My First Heading</h1>");
		htmlFragment.append("<p>My first paragraph.</p>");
		htmlFragment.append("<a href=\"http://www.google.es\" title=\"Prueba\" class=\"btn-torrent\" target=\"_blank\">Descarga tu Archivo torrent!</a>");
		htmlFragment.append("<a href=\"http://www.yahoo.com\" title=\"Prueba\" class=\"btn-torrent\" target=\"_blank\">Descarga tu Archivo torrent!</a>");

		htmlFragment.append("</body>");
		htmlFragment.append("</html>");

		return htmlFragment.toString();
	}

	/**
	 * New HTML with list.
	 *
	 * @param numberListElements the number list elements
	 * @param className the class name
	 * @return the string
	 */
	public String newHTMLWithList ( final int numberListElements,
			final String 	className ) {

		final StringBuilder htmlString =new StringBuilder(
				"<ul class=\"" + className + "\">\n");
		for (int i=0;i<numberListElements;i++) {
			final String htmlElement=String.format(
					"<li>\n"
							+ "<h2>%s</h2>\n"
							+ "</li>\n",
							"elementList_"+ i);

			htmlString.append(htmlElement);
		}
		return htmlString + "</ul>";
	}

	/**
	 * Gets the HTML by URL.
	 *
	 * @param jsoupHelper the jsoup helper
	 * @param URL the url
	 * @return the HTML by URL
	 */
	public String getHTMLByURL (final JSoupHelper jsoupHelper, final String URL){
		return jsoupHelper.newInstanceByURL(URL).html();
	}
}

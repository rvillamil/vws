/**
 *
 */
package es.rvp.web.vws.utils;

/**
 * @author Rodrigo
 *
 */
public enum HTMLFactorySingleton {
	INSTANCE;

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

	public String newHTMLFragmentWithBody (final String body) {
		final StringBuilder htmlFragment=new StringBuilder("<!DOCTYPE html>");
		htmlFragment.append("<html>");
		htmlFragment.append("<body>");
		htmlFragment.append(body);
		htmlFragment.append("</body>");
		htmlFragment.append("</html>");

		return htmlFragment.toString();
	}

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

	public String newHTMLWithList ( final int numberListElements,
			final String 	className ) {

		final StringBuilder htmlString =new StringBuilder(
				"<ul class=\"" + className + "\">\n");
		for (int i=0;i<numberListElements;i++) {
			final String htmlElement=String.format(
					"<li>\n"
							+ "<h2>%s</h2>\n"
							+ "</li>\n",
							( "elementList_"+ i));

			htmlString.append(htmlElement);
		}
		return htmlString + "</ul>";
	}
}

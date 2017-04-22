package es.rvp.web.vws.components.jsoup;

import java.io.IOException;
import java.net.URL;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * @see es.rvp.web.vws.components.parsers.jsoup.JSoupHelper
 * @author Rodrigo Villamil Perez
 */
@Component("jSoupHelper")
public class JSoupHelperImpl implements JSoupHelper {

	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger(JSoupHelperImpl.class);

	/* (non-Javadoc)
	 * @see es.rvp.web.vws.components.jsoup.JSoupHelper#newInstanceFromHTMLFragment(java.lang.String)
	 */
	@Override
	public Document newInstanceFromText(final String htmlfragment) {
		return Jsoup.parseBodyFragment(htmlfragment);
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * es.rvp.web.vws.components.jsoup.JSoupHelper#newInstanceByURL(java.lang.
	 * String)
	 */
	@Override
	public Document newInstanceByURL(final String urlString) {
		Document document = null;
		try {
			final URL theURL = new URL(urlString);
			final Connection connection = Jsoup.connect(theURL.toExternalForm());
			if (connection != null) {
				document = connection.get();
			} else {
				LOGGER.info(String.format("Connection error to '%s'", urlString));
			}
		} catch (final IOException ex) {
			LOGGER.info("Couldn't create object Document. Method 'newInstanceByURL' with URL '" + urlString + "'. Exception: " + ex.getClass().getName());
		}

		return document;
	}

	/*
	 * (non-Javadoc)
	 *
	 * @see es.rvp.web.vws.components.jsoup.JSoupHelper#
	 * newInstanceByElementWithURL(org.jsoup.nodes.Element)
	 */
	@Override
	public Document newInstanceFromElementWithURL (final Element element) {
		Document document = null;
		try {
			String urlWithShow = element.select("a").attr("href");
			if (!urlWithShow.isEmpty()) {
				document = this.newInstanceByURL(urlWithShow);
			}
		} catch (Exception ex) {
			LOGGER.warn("newInstanceFromElementWithURL --> " +  ex.getMessage(), ex);
		}

		return document;
	}


	/*
	 * (non-Javadoc)
	 *
	 * @see
	 * es.rvp.web.vws.components.jsoup.JSoupHelper#selectElementsByClassListName
	 * (org.jsoup.nodes.Document, java.lang.String)
	 */
	@Override
	public Elements selectElementsByClassListName(final Document document, final String classWithNameOfList) {
		return document.select("ul." + classWithNameOfList).select("li");
	}

	/* (non-Javadoc)
	 * @see es.rvp.web.vws.components.jsoup.JSoupHelper#selectElementText(org.jsoup.nodes.Document, java.lang.String, int)
	 */
	@Override
	public String selectElementText(final Document document, final String cssQuery, final int index) {
		String elementValue = null;
		try {
			Elements elements = document.select(cssQuery);

			if (!elements.isEmpty()) {
				Element element = document.select(cssQuery).get(index);
				if (element != null) {
					elementValue = element.text().trim();
				}
			}
		} catch (Exception ex) {
			LOGGER.warn("selectElementText --> " + ex.getMessage(), ex);
		}
		return elementValue;
	}

	/* (non-Javadoc)
	 * @see es.rvp.web.vws.components.jsoup.JSoupHelper#getElementTextByClass(org.jsoup.nodes.Document, java.lang.String, int)
	 */
	@Override
	public String getElementTextByClass(final Document document, final String className, final int index) {
		String elementValue = null;
		try {
			Elements elements = document.getElementsByClass(className);

			if (!elements.isEmpty()) {
				Element element = elements.get(index);
				if (element != null) {
					elementValue = element.ownText().trim();
				}
			}
		} catch (Exception ex) {
			LOGGER.warn("getElementTextByClass --> " + ex.getMessage(), ex);
		}

		return elementValue;
	}

	/* (non-Javadoc)
	 * @see es.rvp.web.vws.components.jsoup.JSoupHelper#getElementURLByClass(org.jsoup.nodes.Document, java.lang.String, int)
	 */
	@Override
	public String getElementURLByClass(final Document document, final String className, final int index) {
		String elementValue = null;
		try {
			Elements elements = document.getElementsByClass(className);

			if (!elements.isEmpty()) {
				Element element = elements.get(index);
				if (element != null) {
					elementValue = element.absUrl("href");
				}
			}
		} catch (Exception ex) {
			LOGGER.warn("getElementURLByClass --> " + ex.getMessage(), ex);
		}

		return elementValue;
	}

	/* (non-Javadoc)
	 * @see es.rvp.web.vws.components.jsoup.JSoupHelper#getElementURLIMGByClass(org.jsoup.nodes.Document, java.lang.String, int)
	 */
	@Override
	public String getElementURLIMGByClass(final Document document, final String className, final int index) {
		String elementValue = null;
		try {
			Elements elements = document.getElementsByClass(className);

			if (!elements.isEmpty()) {
				Element element = elements.select ("img").get(index);
				if (element != null) {
					elementValue = element.absUrl("src").trim();
				}
			}
		} catch (Exception ex) {
			LOGGER.warn("getElementURLIMGByClass --> " + ex.getMessage(), ex);
		}

		return elementValue;
	}
}

package es.rvp.web.vws.components.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Tag;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import es.rvp.web.vws.TestConfig;
import es.rvp.web.vws.components.jsoup.JSoupHelper;

/**
 * @author Rodrigo Villamil Perez
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestConfig.class)
public class JsoupHelperIT {

	// Interface a testear
	@Autowired
	private JSoupHelper jsoupHelper;

	//----------------------- newInstanceFromText -----------------------------
	// Covered by Junit Test

	//------------------------ newInstanceByURL -------------------------------
	@Test
	public void givenInvalidURLWhenGetDocumentThenGetNull() {
		// Given
		final String stringURL = "http://prprpr34823423nin320bcsndlcbp.com";

		// When
		final Document document = this.jsoupHelper.newInstanceByURL(stringURL);
		// Then
		assertNull 	( document );
	}

	@Test
	public void givenURLWhenGetDocumentThenGetInfoNotNull() {
		// Given
		final String stringURL = "http://www.google.es";

		// When
		final Document document = this.jsoupHelper.newInstanceByURL(stringURL);
		// Then
		assertNotNull 	( document );
		assertEquals	( document.baseUri(),stringURL);
		assertTrue 		( document.hasText() );
	}

	// -------------------- newInstanceFromElementWithURL ---------------------
	@Test
	public void givenElementWithURLWithoutContentWhenInstanceDocumentThenGetNull() {
		/*
		<a href="http://foto.jpg"
				 title="Descargar Diez Mil Santos gratis">
		*/
		// Given
		final Tag tag1 = Tag.valueOf("a");
		final Attributes attributes1 = new Attributes();
		attributes1.put("href", "http://foto.jpg");
		attributes1.put("title", "pp");

		final Element element1	= new Element (tag1,"http://urlbase,",attributes1);

		// When
		final Document document = this.jsoupHelper.newInstanceFromElementWithURL(element1);

		// Then
		assertNull 	( document );
	}

	@Test
	public void givenElementWithURLWithContentWhenInstanceDocumentThenGetTheDocument() {
		/*
		<a href="http://www.google.es"
				 title="Descargar Diez Mil Santos gratis">
		*/
		// Given
		String urlWithContent = "http://www.google.es";
		final Tag tag1 = Tag.valueOf("a");
		final Attributes attributes1 = new Attributes();
		attributes1.put("href", urlWithContent);
		attributes1.put("title", "pp");

		final Element element1	= new Element (tag1,"http://urlbase,",attributes1);

		// When
		final Document document = this.jsoupHelper.newInstanceFromElementWithURL(element1);

		// Then
		assertNotNull 	( document );
		assertEquals 	( document.baseUri(), urlWithContent );
		assertTrue 		( document.hasText());
	}

	@Test
	public void givenElementComplexWithURLWithContentWhenInstanceDocumentThenGetTheDocument() {
		/*
	  	 <li>
			<a href="http://www.google.es"
				title="pp">Correcto</a>
		  </li>
		*/
		// Given
		String urlWithContent = "http://www.google.es";

		final Tag tag1 = Tag.valueOf("li");
		final Tag tag2 = Tag.valueOf("a");
		final Attributes attributes1 = new Attributes();
		final Attributes attributes2 = new Attributes();
		attributes2.put("href", urlWithContent);
		attributes2.put("title", "pp");


		final Element element1	= new Element (tag1,"http://prueba,",attributes1);
		final Element element2 	= new Element (tag2,"http://prueba,",attributes2);
		element2.appendText("Correcto");
		element1.appendChild(element2);

		// When
		final Document document = this.jsoupHelper.newInstanceFromElementWithURL(element1);

		// Then
		assertNotNull 	( document );
		assertEquals 	( document.baseUri(),  urlWithContent );
		assertTrue 		( document.hasText() );
	}

	// --------------------------- selectElementText --------------------------
	// Covered by unit test

	// -------------------------- getElementTextByClass -----------------------
	// Covered by unit test

	// -------------------------- getElementURLByClass ------------------------
	// Covered by unit test
}

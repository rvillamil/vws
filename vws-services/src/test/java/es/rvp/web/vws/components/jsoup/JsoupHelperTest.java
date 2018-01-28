package es.rvp.web.vws.components.jsoup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Before;
import org.junit.Test;

import es.rvp.web.vws.utils.HTMLFactorySingleton;

// TODO: Auto-generated Javadoc
/**
 * The Class JsoupHelperTest.
 *
 * @author Rodrigo Villamil Perez
 */
public class JsoupHelperTest {
	
	/** The jsoup helper. */
	// Clases a testear
	private JSoupHelper jsoupHelper;

	/**
	 * Setup.
	 */
	@Before
	public void setup() {
		this.jsoupHelper  		= new JSoupHelperImpl ();
	}

	/**
	 * Given any text then create HTML fragment.
	 */
	//----------------------- newInstanceFromText -----------------------------
	@Test
	public void givenAnyTextThenCreateHTMLFragment() {
		// Given
		String theText 		   = "test";
		String htmlText = "<html>\n <head></head>\n <body>\n  test\n </body>\n</html>";

		Document document = this.jsoupHelper.newInstanceFromText(theText);
		// Then
		assertNotNull 	( document );
		assertEquals 	( document.html(), htmlText );
	}

	//------------------------ newInstanceByURL -------------------------------
	// Covered by Integration Test
	// -------------------- newInstanceFromElementWithURL ---------------------
	// Covered by Integration Test
	/**
	 * Given document with HTML list when parse then get the elements in the list.
	 */
	// -------------------- selectElementsByClassListName ---------------------
	@Test
	public void givenDocumentWithHTMLListWhenParseThenGetTheElementsInTheList() {
		/*
		<ul class="className">
		  		<li>one</li>
		  		<li>thow</li> ....
		  		<li>n</li>
		 </ul>
		 */
		// Given
		String HTMList = HTMLFactorySingleton.INSTANCE.newHTMLWithList (5, "className");
		Document document = Jsoup.parseBodyFragment(HTMList);
		// When
		Elements elements = this.jsoupHelper.selectElementsByClassListName(document, "className");
		// Then
		assertNotNull  ( elements);
		assertTrue 	( elements.size() == 5);
	}

	/**
	 * Given document with out HTML list when parse then get empty list not null.
	 */
	@Test
	public void givenDocumentWithOutHTMLListWhenParseThenGetEmptyListNotNull() {
		/*
		<ul class="className">
		</ul>
		 */
		// Given
		final StringBuilder HTMList=new StringBuilder(
				"<ul class=\"className\">\n<\\ul>");
		Document document = Jsoup.parseBodyFragment(HTMList.toString());

		// When
		Elements elements = this.jsoupHelper.selectElementsByClassListName(document, "className");
		// Then
		assertNotNull  ( elements);
		assertTrue 	( elements.size() == 0);
	}

	/**
	 * Given document with invalid HTML list when parse then get empty list not null.
	 */
	@Test
	public void givenDocumentWithInvalidHTMLListWhenParseThenGetEmptyListNotNull() {
		/*
		<a href="http://www.google.es"
				 title="Descargar Diez Mil Santos gratis">
		</a>
		 */
		// Given
		final StringBuilder HTMList=new StringBuilder(
				"<a href=\"http://www.google.es\" title=\"prueba\">\n<\\a>");
		Document document = Jsoup.parseBodyFragment(HTMList.toString());

		// When
		Elements elements = this.jsoupHelper.selectElementsByClassListName(document, "className");
		// Then
		assertNotNull  ( elements);
		assertTrue 	( elements.size() == 0);
	}

	/**
	 * Given HTML fragment with two paragraph when select the second get the text.
	 */
	// --------------------------- selectElementText --------------------------
	@Test
	public void givenHTMLFragmentWithTwoParagraphWhenSelectTheSecondGetTheText() {
		// Given
		String htmlFragment = HTMLFactorySingleton.INSTANCE.newHTMLFragment();
		Document document = Jsoup.parseBodyFragment(htmlFragment);
		// When
		String theText = this.jsoupHelper.selectElementText(document, "p", 1);
		// Then
		assertEquals (theText, "My second paragraph.");
	}

	/**
	 * Given HTML fragment with two paragraph when select the third get null.
	 */
	@Test
	public void givenHTMLFragmentWithTwoParagraphWhenSelectTheThirdGetNull() {
		// Given
		String htmlFragment = HTMLFactorySingleton.INSTANCE.newHTMLFragment();
		Document document = Jsoup.parseBodyFragment(htmlFragment);
		// When
		String theText = this.jsoupHelper.selectElementText(document, "p", 3);
		// Then
		assertNull (theText);
	}

	/**
	 * Given HTML fragment with two paragraph when select no text element then get null.
	 */
	@Test
	public void givenHTMLFragmentWithTwoParagraphWhenSelectNoTextElementThenGetNull() {
		// Given
		String htmlFragment = HTMLFactorySingleton.INSTANCE.newHTMLFragment();
		Document document = Jsoup.parseBodyFragment(htmlFragment);
		// When
		String theText = this.jsoupHelper.selectElementText(document, "body", 1);
		String theText2 = this.jsoupHelper.selectElementText(document, "inexistente", 1);

		// Then
		assertNull (theText);
		assertNull (theText2);
	}
	
	/**
	 * Given HTML fragment with the same twho class when get the second class get the text.
	 */
	// -------------------------- getElementTextByClass -----------------------
	@Test
	public void givenHTMLFragmentWithTheSameTwhoClassWhenGetTheSecondClassGetTheText() {
		// Given
		String htmlFragment = HTMLFactorySingleton.INSTANCE.newHTMLWithClassFragment();
		Document document = Jsoup.parseBodyFragment(htmlFragment);
		// When
		String theText = this.jsoupHelper.getElementTextByClass(document, "claseTest", 1);
		// Then
		assertEquals (theText, "Hello2");
	}

	/**
	 * Given HTML fragment with the same twho class when get the third class get null.
	 */
	@Test
	public void givenHTMLFragmentWithTheSameTwhoClassWhenGetTheThirdClassGetNull() {
		// Given
		String htmlFragment = HTMLFactorySingleton.INSTANCE.newHTMLWithClassFragment();
		Document document = Jsoup.parseBodyFragment(htmlFragment);
		// When
		String theText = this.jsoupHelper.getElementTextByClass(document, "claseTest", 2);
		// Then
		assertNull (theText);
	}

	/**
	 * Given HTML fragment with the same twho class when get inexistent class get null.
	 */
	@Test
	public void givenHTMLFragmentWithTheSameTwhoClassWhenGetInexistentClassGetNull() {
		// Given
		String htmlFragment = HTMLFactorySingleton.INSTANCE.newHTMLWithClassFragment();
		Document document = Jsoup.parseBodyFragment(htmlFragment);
		// When
		String theText = this.jsoupHelper.getElementTextByClass(document, "Inexistent", 1);
		// Then
		assertNull (theText);
	}

	/**
	 * Given HTML fragment with the same two URL class when get the second class get the text.
	 */
	// -------------------------- getElementURLByClass ------------------------
	@Test
	public void givenHTMLFragmentWithTheSameTwoURLClassWhenGetTheSecondClassGetTheText() {
		// Given
		String htmlFragment = HTMLFactorySingleton.INSTANCE.newHTMLWithURLClassFragment();
		Document document = Jsoup.parseBodyFragment(htmlFragment);
		// When
		String theText = this.jsoupHelper.getElementURLByClass(document, "btn-torrent", 0);
		// Then
		assertEquals (theText, "http://www.google.es");
	}

	/**
	 * Given HTML fragment with the same two URL class when get the third class get null.
	 */
	@Test
	public void givenHTMLFragmentWithTheSameTwoURLClassWhenGetTheThirdClassGetNull() {
		// Given
		String htmlFragment = HTMLFactorySingleton.INSTANCE.newHTMLWithURLClassFragment();
		Document document = Jsoup.parseBodyFragment(htmlFragment);
		// When
		String theText = this.jsoupHelper.getElementURLByClass(document, "btn-torrent", 2);
		// Then
		assertNull (theText);
	}

	/**
	 * Given HTML fragment with the same two URL class when get inexistent class get null.
	 */
	@Test
	public void givenHTMLFragmentWithTheSameTwoURLClassWhenGetInexistentClassGetNull() {
		// Given
		String htmlFragment = HTMLFactorySingleton.INSTANCE.newHTMLWithURLClassFragment();
		Document document = Jsoup.parseBodyFragment(htmlFragment);
		// When
		String theText = this.jsoupHelper.getElementURLByClass(document, "Inexistent", 2);
		// Then
		assertNull (theText);
	}

	/**
	 * Given HTML with IMG list when parse by class name then get the UR lfor first img.
	 */
	// --------------------- getElementURLIMGByClass --------------------------
	@Test
	public void givenHTMLWithIMGListWhenParseByClassNameThenGetTheURLforFirstImg() {
		// Given
		String htmlFragment = HTMLFactorySingleton.INSTANCE.newHTMLWithIMGFragment();
		Document document = Jsoup.parseBodyFragment(htmlFragment);
		// When
		String theText = this.jsoupHelper.getElementURLIMGByClass(document, "entry-left", 0);
		// Then
		assertNotNull (theText);
		assertEquals ( theText, "http://www.prueba1.jpg");
	}

	/**
	 * Given HTML with IMG list when parse and get the third element by class name then get nul.
	 */
	@Test
	public void givenHTMLWithIMGListWhenParseAndGetTheThirdElementByClassNameThenGetNul() {
		// Given
		String htmlFragment = HTMLFactorySingleton.INSTANCE.newHTMLWithIMGFragment();
		Document document = Jsoup.parseBodyFragment(htmlFragment);
		// When
		String theText = this.jsoupHelper.getElementURLIMGByClass(document, "entry-left", 3);
		// Then
		assertNull (theText);
	}

	/**
	 * Given HTML with IMG list when parse by wrong class name then get nul.
	 */
	@Test
	public void givenHTMLWithIMGListWhenParseByWrongClassNameThenGetNul() {
		// Given
		String htmlFragment = HTMLFactorySingleton.INSTANCE.newHTMLWithIMGFragment();
		Document document = Jsoup.parseBodyFragment(htmlFragment);
		// When
		String theText = this.jsoupHelper.getElementURLIMGByClass(document, "wrong", 0);
		// Then
		assertNull (theText);
	}
}

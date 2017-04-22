package es.rvp.web.vws.services.tumejortorrent;

import static org.mockito.Mockito.mock;

import org.junit.Before;
import org.junit.Ignore;

import es.rvp.web.vws.components.jsoup.JSoupHelper;
import es.rvp.web.vws.components.jsoup.JSoupHelperImpl;
import es.rvp.web.vws.domain.ShowFactory;
import es.rvp.web.vws.services.WebTorrentSpider;

/*
 * @author Rodrigo Villamil Perez
 */
@Ignore // TODO 08-c: Test Unitario - Completar
public class WebTorrentSpiderTest {

	// Interface a testear
	private WebTorrentSpider 	webTorrentSpider;

	// Clases a mockear
	private JSoupHelper 			jSoupHelper;
	private ShowFactory				showFactory;

	@Before
	public void setup() {
		this.jSoupHelper 		= mock (JSoupHelperImpl.class);
		//this.showFactory  	= new ShowFactoryImpl (this.jSoupHelper);
	}
}

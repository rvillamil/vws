package es.rvp.web.vws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import es.rvp.web.vws.components.jsoup.JSoupHelper;
import es.rvp.web.vws.components.jsoup.JSoupHelperImpl;
import es.rvp.web.vws.domain.ShowFactory;
import es.rvp.web.vws.domain.ShowFieldParser;
import es.rvp.web.vws.domain.tumejortorrent.ShowEpisodeParser;
import es.rvp.web.vws.domain.tumejortorrent.ShowFactoryImpl;
import es.rvp.web.vws.domain.tumejortorrent.ShowQualityParser;
import es.rvp.web.vws.domain.tumejortorrent.ShowSessionParser;
import es.rvp.web.vws.domain.tumejortorrent.ShowURLToDownloadParser;
import es.rvp.web.vws.services.WebTorrentSpider;
import es.rvp.web.vws.services.tumejortorrent.WebTorrentSpiderImpl;


/**
 * "application-context" for testing purposes.
 *
 * @author  Rodrigo Villamil Perez
 * @Configuration  anotation: "Tags the class as a source of bean definitions for the application context."
 */
@ComponentScan("es.rvp.web.vws")
@Configuration
public class TestConfig {

	/**
	 * J soup helper.
	 *
	 * @return the j soup helper
	 */
	@Bean
	JSoupHelper jSoupHelper() {
		return new JSoupHelperImpl();
	}

	/**
	 * Show episode parser.
	 *
	 * @return the show field parser
	 */
	@Bean
	ShowFieldParser showEpisodeParser() {
		return new ShowEpisodeParser(this.jSoupHelper());
	}

	/**
	 * Show quality parser.
	 *
	 * @return the show field parser
	 */
	@Bean
	ShowFieldParser showQualityParser() {
		return new ShowQualityParser(this.jSoupHelper());
	}

	/**
	 * Show session parser.
	 *
	 * @return the show field parser
	 */
	@Bean
	ShowFieldParser showSessionParser() {
		return new ShowSessionParser(this.jSoupHelper());
	}

	/**
	 * Show URL to download parser.
	 *
	 * @return the show field parser
	 */
	@Bean
	ShowFieldParser showURLToDownloadParser() {
		return new ShowURLToDownloadParser();
	}

	/**
	 * Show factory.
	 *
	 * @return the show factory
	 */
	@Bean
	ShowFactory showFactory() {
		return new ShowFactoryImpl(
				this.jSoupHelper(),
				this.showEpisodeParser(),
				this.showQualityParser(),
				this.showSessionParser(),
				this.showURLToDownloadParser());
	}

	/**
	 * Web torrent spider.
	 *
	 * @return the web torrent spider
	 */
	@Bean
	WebTorrentSpider webTorrentSpider() {
		return new WebTorrentSpiderImpl(
				this.jSoupHelper(),
				this.showFactory());
	}

}

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
 * "application-context" for testing purposes
 * @Configuration  anotation: "Tags the class as a source of bean definitions for the application context."
 * @author  Rodrigo Villamil Perez
 */
@ComponentScan("es.rvp.web.vws")
@Configuration
public class TestConfig {
	@Bean
	JSoupHelper jSoupHelper() {
		return new JSoupHelperImpl();
	}

	@Bean
	ShowFieldParser showEpisodeParser() {
		return new ShowEpisodeParser(this.jSoupHelper());
	}

	@Bean
	ShowFieldParser showQualityParser() {
		return new ShowQualityParser(this.jSoupHelper());
	}

	@Bean
	ShowFieldParser showSessionParser() {
		return new ShowSessionParser(this.jSoupHelper());
	}

	@Bean
	ShowFieldParser showURLToDownloadParser() {
		return new ShowURLToDownloadParser();
	}

	@Bean
	ShowFactory showFactory() {
		return new ShowFactoryImpl(
				this.jSoupHelper(),
				this.showEpisodeParser(),
				this.showQualityParser(),
				this.showSessionParser(),
				this.showURLToDownloadParser());
	}

	@Bean
	WebTorrentSpider webTorrentSpider() {
		return new WebTorrentSpiderImpl(
				this.jSoupHelper(),
				this.showFactory());
	}
}

package es.rvp.web.vws;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * "application-context" for testing purposes
 *
 * @Configuration anotation: "Tags the class as a source of bean definitions for
 * the application context."
 *
 * @author Rodrigo Villamil Perez
 */
@Configuration
@ComponentScan("es.rvp.web.vws")
public class TestConfig {

	/*
	 * No es necesario porque tenemos el Autowired en cada clase/componente. Pero si hubiese conflictos lo configuramos
	 * aqui de la siguiente forma:
	 *
	@Bean
	JSoupHelper jSoupHelper() {
		return new JSoupHelperImpl();
	}

	@Bean
	ShowFactory showFactory() {
		return new ShowFactoryImpl(jSoupHelper());
	}

	@Bean
	WebTorrentSpider webTorrentSpider() {
		return new WebTorrentSpiderImpl(jSoupHelper(), showFactory());
	}
	*/
}

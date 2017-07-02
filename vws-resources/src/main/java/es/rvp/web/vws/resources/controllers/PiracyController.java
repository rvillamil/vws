package es.rvp.web.vws.resources.controllers;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import es.rvp.web.vws.domain.Show;
import es.rvp.web.vws.services.WebTorrentSpider;

/**
 * Rest Controller
 * In Spring’s approach to building RESTful web services, HTTP requests are handled by a controller.
 * @author Rodrigo Villamil Pérez
 */
@RestController
@ConfigurationProperties(prefix="general")
@CrossOrigin(origins = "http://localhost:9090")
public class PiracyController {

	// TODO 00: PMV Produccion
	/*
	Backend
			- Subir a Azure gratis
			- Servicios rest bien hechos antes de acabar el Front: 	http://websystique.com/spring-boot/spring-boot-rest-api-example/
				- PiracyController no estan muy bien...no es muy rest..

				- Ojo PUT , POST e idempotencia , optimistic lock ...¿Como evitamos problemas de concurrencia?
					- http://labs.unacast.com/2016/02/25/on-idempotency-in-distributed-rest-apis/
					- https://spring.io/guides/tutorials/bookmarks/

			- Test unitarios para los controladores REST, persistencia..todo lo que falte

			- docker para el front end ..
			- Acabar el Front bien de una vez al cambiar los servicios
			- oAuth: Crear usuarios y soporte para OAuth con Google:
				Tnemos un ejemplo en: https://spring.io/guides/tutorials/bookmarks/

		- Revisar la configuracion de spring boot y la carga de properties mas interesantes
	 	      - https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html
	          - http://www.baeldung.com/spring-boot-application-configuration

		- Revisar bien Spring boot actuator: http://www.baeldung.com/spring-boot-actuators?utm_content=buffer309af&utm_medium=social&utm_source=twitter.com&utm_campaign=buffer
    */


	// TODO 01: Jenkins CI - Finalizar el soporte para Docker de la siguiente forma:
	//
	//
	/*
	// ....Jenkisfile
	stage('Create Docker Image') {
	    dir('webapp') {
	      docker.build("arungupta/docker-jenkins-pipeline:${env.BUILD_NUMBER}")
	    }
	  }
	 */
	//
	// TODO 02: Funcionalidades de negocio
	// * Descarga de pelis cuando salgan en una calidad determinada. Por ejemplo, “Reservar Spiderman” y cuando Spiderman salga y ademas en la calidad que pongamos, la pondrá a descargar.
	// * Notas de las pelis: Implementar el parser de filmaffinity  o http://www.cinesift.com/  —> Casi mejor usar una API pública de metracritic o similar ( https://www.publicapis.com/ )
	//
	// LOGGER
	private static final Logger LOGGER 	= LoggerFactory.getLogger(PiracyController.class);
	private static final String	WELCOME_TEXT   ="Video websites scaper (VWS) is available!";
	//
	// Area de datos
	//
	@Value("${general.scraping.parse.maxbillboardfilms}")
	public int 						maxBillboardFilms;
	@Value("${general.scraping.parse.maxvideopremieres}")
	public int 						maxVideoPremieres;
	@Value("${general.scraping.parse.maxtvshows}")
	public int 						maxTVshows;

	@Autowired
	private final WebTorrentSpider 		webTorrentSpider;
	/**
	 * Constructor
	 * @param webTorrentSpider web torrent spider service
	 */
	public PiracyController(final WebTorrentSpider webTorrentSpider) {
		super();
		this.webTorrentSpider = webTorrentSpider;
	}
	/**
	 * @return One text if this web site is available
	 */
	@RequestMapping(value="/",
					method=RequestMethod.GET)
	String hello() {
		LOGGER.info(WELCOME_TEXT);
		return WELCOME_TEXT;
	}
	/**
	 * Parse the torrent portal for 'scraping' the billboard
	 *
	 * @return JSon object, with the billboard films in the torrent portal [0, maxBillboardFilms]
	 */
	@RequestMapping(value="/billboardfilms",
					method=RequestMethod.GET)
	public Set<Show> parseBillBoardFilms() {
		LOGGER.info("Getting billboard films ...");
		return this.webTorrentSpider.parseBillboardFilms(this.maxBillboardFilms);
	}

	/**
	 * Parse the torrent portal for 'scraping' the video premieres
	 *
	 * @return JSon object, with the video premieres in the torrent portal [0, maxSize]
	 */
	@RequestMapping(value="/videopremieres",
					method=RequestMethod.GET)
	public Set<Show> parseVideoPremieres() {
		LOGGER.info("Getting video premieres ...");
		return this.webTorrentSpider.parseVideoPremieres(this.maxVideoPremieres);
	}

	/**
	 * Parse one TV show from torrent portal, to get the last tv shows from the session
	 *
	 * @param  tvShowName the name of the the tv show
	 * 	 	   e.g: 'modern-family' from http://tumejortorrent.com/series-hd/modern-family)
	 *
	 * @return JSon object, with last episodes from TV show between '0 and maxTVshows'
	 */
	@RequestMapping(value="/tvshows",
					method=RequestMethod.GET)
	public Set<Show> parseTVShow(@RequestParam("name") final String tvShowName) {
		LOGGER.info("Getting the tv show '{}'", tvShowName);
		return this.webTorrentSpider.parseTVShow(tvShowName, this.maxTVshows);
	}
}

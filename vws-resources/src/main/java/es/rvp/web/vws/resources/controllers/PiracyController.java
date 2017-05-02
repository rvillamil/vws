package es.rvp.web.vws.resources.controllers;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.RequestMapping;
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
public class PiracyController {

	// TODO 00: Comenzar a montar la parte 'Front' con Angular
	//
	// TODO 01: Implementar la autorizacion a la API en el Backend con OauthZ
	// TODO 01: Implementar la autenticacion (CAS?, otra cosa?)
	// TODO 01: Montar el soporte para Quartz para buscar pelicualas cada 5 minutos. Luego  salvar en BB.DD los resultados lanzados por el quartz. Soport de docker para BB.DD
	//
	// TODO 02: Jenkins CI - Finalizar el soporte para Docker de la siguiente forma:
	//
	/*
	// ....Jenkisfile
	stage('Create Docker Image') {
	    dir('webapp') {
	      docker.build("arungupta/docker-jenkins-pipeline:${env.BUILD_NUMBER}")
	    }
	  }
   */
	// TODO 03: Revisar la configuracion de spring boot y la carga de properties
	// - https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html
	// - http://www.baeldung.com/spring-boot-application-configuration
	//
	// TODO 04: Spring boot actuator: http://www.baeldung.com/spring-boot-actuators?utm_content=buffer309af&utm_medium=social&utm_source=twitter.com&utm_campaign=buffer
	//
	// TODO 05: Funcionalidades de negocio
	// * Descarga de pelis cuando salgan en una calidad determinada. Por ejemplo, “Reservar Spiderman” y cuando Spiderman salga y ademas en la calidad que pongamos, la pondrá a descargar.
	// * Notas de las pelis: Implementar el parser de filmaffinity  o http://www.cinesift.com/  —> Casi mejor usar una API pública de metracritic o similar ( https://www.publicapis.com/ )
	//
	// LOGGER
	private static final Logger LOGGER 	= LoggerFactory.getLogger(PiracyController.class);

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
	 * Parse the torrent portal for 'scraping' the billboard
	 *
	 * @return JSon object, with the billboard films in the torrent portal [0, maxBillboardFilms]
	 */
	@RequestMapping("/billboardfilms")
	public Set<Show> parseBillBoardFilms() {
		LOGGER.info("Getting billboard films ...");
		return this.webTorrentSpider.parseBillboardFilms(this.maxBillboardFilms);
	}

	/**
	 * Parse the torrent portal for 'scraping' the video premieres
	 *
	 * @return JSon object, with the video premieres in the torrent portal [0, maxSize]
	 */
	@RequestMapping("/videopremieres")
	public Set<Show> parseVideoPremieres() {
		LOGGER.info("Getting video premieres ...");
		return this.webTorrentSpider.parseVideoPremieres(this.maxVideoPremieres);
	}

	/**
	 * Parse one TV show from torrent portal, to get the last tv shows from the session
	 *
	 * @param  tvShowPath the relative path in the torrent portal where the tv show is located
	 * 	 	   e.g: /series-hd/modern-family
	 * 				(Full url will be http://tumejortorrent.com/series-hd/modern-family)
	 *
	 * @return JSon object, with last episodes from TV show between '0 and maxTVshows'
	 */
	public Set<Show> parseTVShow(final String tvShowPath) {
		LOGGER.info("Getting tv shows ...");
		return this.webTorrentSpider.parseTVShow(tvShowPath, this.maxTVshows);
	}

	/**
	 * @return One text if this web site is available
	 */
	@RequestMapping("/")
	String hello() {
		LOGGER.info("Application started !!");
		return "Video websites scaper (VWS) is available!";
	}
}

package es.rvp.web.vws.resources.controllers;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import es.rvp.web.vws.domain.Show;
import es.rvp.web.vws.services.WebTorrentSpider;

/**
 * Rest Controller
 * In Spring’s approach to building RESTful web services, HTTP requests are handled by a controller.
 * @author Rodrigo Villamil Pérez
 */
@RestController
@RequestMapping("/api")
@ConfigurationProperties(prefix="general")
@CrossOrigin(origins = "http://localhost:9090")
public class PiracyController {

	/*
	 FIXME 00: Problema con el docker de mysql. El backend no espera a que inicie el mysql ..Ver 'wrapper.sh'
	*/

	/*
	 TODO 00: PMV Produccion: Autenticacion basica, usuario y password en BB.DD - Entender spring security
	  - https://spring.io/guides/tutorials/bookmarks/#_securing_a_rest_service
	 */

	/*
	 TODO: Para subir a GitHub antes:
	  - Finalizar el resto de Test unitarios para los controladores REST, persistencia..todo lo que falte
	    	   http://www.baeldung.com/spring-boot-testing?utm_content=buffer61c1e&utm_medium=social&utm_source=twitter.com&utm_campaign=buffer
	  - Actualizar el README correctamente: https://www.genbetadev.com/software-libre-y-licencias/checklist-para-liberar-un-proyecto-open-source-en-github
	*/

	/*
	 TODO: Autorizar a la aplicacion UI a consumir servicios de mi API con OAuth2
 	  - https://spring.io/guides/tutorials/spring-boot-oauth2/
	 */

	/*
	 TODO: Spring Cloud, Microservicios, Eureka: https://spring.io/blog/2015/07/14/microservices-with-spring
	*/

    /*
	 TODO: Revision del uso de PUT,POST e idempotencia. ¿Como evitamos problemas de concurrencia? "Optimistic lock"
	  - Charla Gus: https://youtu.be/fZo8Zp2otqQ
	  - http://labs.unacast.com/2016/02/25/on-idempotency-in-distributed-rest-apis/
	  - https://spring.io/guides/tutorials/bookmarks/
	  - Best practices for concurrency control in REST APIs: https://goo.gl/Xqqvii
	  - https://stackoverflow.com/questions/30080634/concurrency-in-a-rest-api
    */

	/*
	 TODO: Revisar la configuracion de spring boot y la carga de properties mas interesantes
      - https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html
      - http://www.baeldung.com/spring-boot-application-configuration
	*/

	/*
	 TODO: Redis - PoC
	  - Introduction to Spring Data Redis - https://goo.gl/oegRqu
	  - Introducción a la base de datos NoSQL Redis - https://goo.gl/JBqiHE
	*/

	/*
	 TODO: Spring boot actuator, revision
	  - http://www.baeldung.com/spring-boot-actuators?utm_content=buffer309af&utm_medium=social&utm_source=twitter.com&utm_campaign=buffer
	*/

	/*
	 TODO: Jenkins CI and Docker - Finalizar el soporte para Docker, de la siguiente forma:
	  ....Jenkisfile
 	  stage('Create Docker Image') {
	      dir('webapp') {
	       docker.build("arungupta/docker-jenkins-pipeline:${env.BUILD_NUMBER}")
	    }
	   }
	*/

	/*
	 TODO: Funcionalidades de negocio
	  - Descarga de pelis cuando salgan en una calidad determinada. Por ejemplo, “Reservar Spiderman” y cuando
	    Spiderman salga y ademas en la calidad que pongamos, la pondrá a descargar.

	  - Poner las notas de las pelis:
	       * Implementar el parser de filmaffinity o bien,
	       * http://www.cinesift.com/
	       * Usar una API pública de metracritic o similar ( https://www.publicapis.com/ )
	*/

	/*
	 TODO: Revisar la configuracion del apache y el tomcat embebidos
	  - https://elpesodeloslunes.wordpress.com/2014/09/07/el-servidor-tomcat-desde-cero-3-configuracion-basica/
	*/


	/*
	 TODO: Probar mutation Testing
	  - https://www.adictosaltrabajo.com/tutoriales/mutation-testing-con-pit/
	 */

	// LOGGER
	private static final Logger LOGGER 			= LoggerFactory.getLogger(PiracyController.class);
	private static final String	WELCOME_TEXT   	= "Video websites scaper (VWS) is available!";
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
	@RequestMapping(value="/billboardfilms/",
					method=RequestMethod.GET)
	public ResponseEntity<?> parseBillBoardFilms() {
		LOGGER.info("PiracyController - Getting billboard films ...");
		Set<Show> shows = this.webTorrentSpider.parseBillboardFilms(this.maxBillboardFilms);
		if ( shows.isEmpty()) {
			LOGGER.warn("PiracyController - Billbaord films not found.");
            return new ResponseEntity<>(new CustomErrorType(
            		"Billboard films list is empty"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shows, HttpStatus.OK);
	}

	/**
	 * Parse the torrent portal for 'scraping' the video premieres
	 *
	 * @return JSon object, with the video premieres in the torrent portal [0, maxSize]
	 */
	@RequestMapping(value="/videopremieres/",
					method=RequestMethod.GET)
	public ResponseEntity<?> parseVideoPremieres() {
		LOGGER.info("PiracyController - Getting video premieres ...");
		Set<Show> shows = this.webTorrentSpider.parseVideoPremieres(this.maxVideoPremieres);
		if ( shows.isEmpty()) {
			LOGGER.warn("PiracyController - Video premieres not found.");

            return new ResponseEntity<>(new CustomErrorType(
            		"Video premieres list is empty"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shows, HttpStatus.OK);
	}

	/**
	 * Parse one TV show from torrent portal, to get the last tv shows from the session
	 *
	 * @param  tvShowName the name of the the tv show
	 * 	 	   e.g: 'modern-family' from http://tumejortorrent.com/series-hd/modern-family)
	 *
	 * @return JSon object, with last episodes from TV show between '0 and maxTVshows'
	 */
	@RequestMapping(value="/tvshows/{title}",
					method=RequestMethod.GET)
	public ResponseEntity<?> parseTVShow(@PathVariable("title") final String title) {
		LOGGER.info("PiracyController - Getting the tvshow '{}'", title);
		Set<Show> shows =  this.webTorrentSpider.parseTVShow(title, this.maxTVshows);

		if ( shows.isEmpty()) {
			LOGGER.warn("PiracyController - TVShows episodes with title '{}' not found.", title);

            return new ResponseEntity<>(new CustomErrorType(
            		"TVShows list is empty"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shows, HttpStatus.OK);
	}

}

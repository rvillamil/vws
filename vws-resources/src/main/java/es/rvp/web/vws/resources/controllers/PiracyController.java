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
@CrossOrigin(origins = "http://localhost:8080")
public class PiracyController {

	// TODO 00: Las tareas de refactorizacion son las siguientes:
   /*
	Backend

		- Spring profiles: Producción con docker. Desarrollo con HSQL
		  Que funcione con Docker!
		  	--> https://g00glen00b.be/docker-spring-boot/

		- Docker: DUDA? El Mysql docker lo metemos dentro del proyecto persistence que queda mejor.
		  Asi tenemos el SQL a mano tambien para tocarlo. Ver: https://spring.io/guides/gs/accessing-data-mysql/

		- Revisar la configuracion de spring boot y la carga de properties
	 	      - https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html
	          - http://www.baeldung.com/spring-boot-application-configuration

		- REVISAR los controladores REST ¿Siguen las normas basicas? ¿Cuando hacemos un post no deberia de crear un nuevo obejto?


		- Revisar bien Spring boot actuator: http://www.baeldung.com/spring-boot-actuators?utm_content=buffer309af&utm_medium=social&utm_source=twitter.com&utm_campaign=buffer

	Frontend
		Al entrar en la sección de favoritos vamos al servidor , los cargamos y los mostramos
		Para añadir favoritos hacemos los siguiente: buscamos con la lista de favoritos para ver si ya lo tenemos . Si no lo tenemos , buscamos en el portal de torrents si existe el tvshow. Si existe lo añadimos a favoritos
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
	// TODO 02: Implementar la autorizacion a la API en el Backend con OauthZ
	// TODO 03: Implementar la autenticacion (CAS?, otra cosa?)
	// TODO 04: Montar el soporte para Quartz para buscar pelicualas cada 5 minutos. Luego  salvar en BB.DD los resultados lanzados por el quartz. Soport de docker para BB.DD
	// TODO 05: Funcionalidades de negocio
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

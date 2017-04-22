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

	//
	// FIXME 00: Se siguen lanzando los test de integracion aunque use: mvn install -Pdevelop,-docker-support ¿Que pasa?!! Es el tema del Jacoco pero no se que es
	//
	// TODO 00: Jenkins CI - Montar el soporte para Docker de la siguiente forma:
	//
	/*
	 Quizas la mejor opcion sea dejar para el perfil 'develop', la generacion de imagenes con maven tal y como esta.
	 para el Perfil de 'integration', excluir esa generacion que se hace en desarrollo y
	 montarla en la integracion continua con el Jenkinsfile sin el soporte de maven:
	  	mvn install -P develop,-docker-support (Ojo con el '-' delante excluye el profile)

	// ....Jenkisfile
	stage('Create Docker Image') {
	    dir('webapp') {
	      docker.build("arungupta/docker-jenkins-pipeline:${env.BUILD_NUMBER}")
	    }
	  }
   */
	// TODO 01: Jenkins CI - Finalizar la integración continua con en mi macbook air
	//		- Revisar que mas cosas podemos hacer con los pipelines (Ver mis favoritos sobre pipeline-CI)
	//		- Pasar las metricas de calidad
	// 		- Montar un entorno de pruebas sodbre docker. Publicar el docker resultante en Docker Hub privado
	// 						* Docker hub is a repository of images that can be public or private.
	// 						  It literally just stores images. That is it. It's the default namespace if you don't define
	//						  your own registry "docker pull nginx" vs "docker pull swozey.com/nginx."
	//
	//						* Docker Cloud is their SAAS service that runs docker containers for hosting applications/etc.
	//						  It's akin to an AWS/GKE
	// 						- Ver , Docker hub, Docker Store, Cloud docker
	//
	//
	// TODO 02: Revisar la configuracion de spring boot y la carga de properties ( https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html)
	//
	//
	// TODO 03: Montar el soporte para Quartz para buscar pelicualas cada 5 minutos.
	// 			Luego  salvar en BB.DD los resultados lanzados por el quartz. Soport de docker para BB.DD
	//
	// TODO 04: Montar el README explicando como se instala o como se desarrolla

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

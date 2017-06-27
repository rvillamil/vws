package es.rvp.web.vws.resources.controllers;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import es.rvp.web.vws.domain.Favorite;
import es.rvp.web.vws.domain.FavoriteRepository;
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

	// FiXME 00: Una opcion es cargar los favoritos en el navegador, y antes de hacer una busqueda, consultar si ya los tienes en el navegador y pasar de hacerla contra el servidor...
	// TODO 00: Montar la parte 'Front' con Angular
	// TODO 00: Soporte para Cors : https://spring.io/guides/gs/rest-service-cors/
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
	// TODO 04: Reviar bien Spring boot actuator: http://www.baeldung.com/spring-boot-actuators?utm_content=buffer309af&utm_medium=social&utm_source=twitter.com&utm_campaign=buffer
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

	@Autowired
	private FavoriteRepository 			favoriteRepository;
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

	/**
	 * @return One text if this web site is available
	 */
	@RequestMapping(value="/",
					method=RequestMethod.GET)
	String hello() {
		LOGGER.info("VWS application started !!");
		return "Video websites scaper (VWS) is available!";
	}


	@GetMapping(path="/add") // Map ONLY GET Requests
	@ResponseBody
	public Favorite addNewFavorite (@RequestParam( final String title) {
		// @ResponseBody means the returned String is the response, not a view name
		// @RequestParam means it is a parameter from the GET or POST request

		Favorite favorite = new Favorite();
		favorite.setTitle(title);
		return favorite;
	}

}

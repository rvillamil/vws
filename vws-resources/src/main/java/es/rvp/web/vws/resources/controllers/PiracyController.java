package es.rvp.web.vws.resources.controllers;

import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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
@RequestMapping("/api")
@ConfigurationProperties(prefix="general")
// @CrossOrigin(origins = "http://localhost:9090")
public class PiracyController {


    /*
      FIXME 01: No tiene buena pinta de estar bien configurado el soporte para spring-boot-actuator no se si es por el filtro de spring security.
      - https://github.com/spring-projects/spring-boot/issues/8255 , actuator tiene muchos mas endopints (/health .. /heapdump ..etc)
      - http://www.baeldung.com/spring-boot-actuators?utm_content=buffer309af&utm_medium=social&utm_source=twitter.com&utm_campaign=buffer
    */
    /*
     FIXME 02: Aligerar los contenedores docker con ALPINE. ejemplo : https://stackoverflow.com/questions/39967945/how-do-i-wait-for-a-db-container-to-be-up-before-my-spring-boot-app-starts
     */

    /*
     FIXME 03: Configurar bien Jacoco o bien Cobertura para que me indique la cobertura de los mas de 50 test que tengo
     */
    /*
     TODO 00: Finalizar el resto de Test unitarios para los controladores REST, persistencia..todo lo que falte
               http://www.baeldung.com/spring-boot-testing?utm_content=buffer61c1e&utm_medium=social&utm_source=twitter.com&utm_campaign=buffer
     */

    /*
     TODO 01: Actualizar el README correctamente: https://www.genbetadev.com/software-libre-y-licencias/checklist-para-liberar-un-proyecto-open-source-en-github
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
    @GetMapping(value="/")
    String hello() {
        LOGGER.info(WELCOME_TEXT);
        return WELCOME_TEXT;
    }

    /**
     * Parse the torrent portal for 'scraping' the billboard
     *
     * @return JSon object, with the billboard films in the torrent portal [0, maxBillboardFilms]
     */
    @GetMapping(value="/billboardfilms/")
    public ResponseEntity<?> parseBillBoardFilms() {
        LOGGER.info("PiracyController - Getting billboard films ...");
        final Set<Show> shows = webTorrentSpider.parseBillboardFilms(maxBillboardFilms);
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
    @GetMapping(value="/videopremieres/")
    public ResponseEntity<?> parseVideoPremieres() {
        LOGGER.info("PiracyController - Getting video premieres ...");
        final Set<Show> shows = webTorrentSpider.parseVideoPremieres(maxVideoPremieres);
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
    @GetMapping(value="/tvshows/{title}")
    public ResponseEntity<?> parseTVShow(@PathVariable("title") final String title) {
        LOGGER.info("PiracyController - Getting the tvshow '{}'", title);
        final Set<Show> shows =  webTorrentSpider.parseTVShow(title, maxTVshows);

        if ( shows.isEmpty()) {
            LOGGER.warn("PiracyController - TVShows episodes with title '{}' not found.", title);

            return new ResponseEntity<>(new CustomErrorType(
                    "TVShows list is empty"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shows, HttpStatus.OK);
    }
}

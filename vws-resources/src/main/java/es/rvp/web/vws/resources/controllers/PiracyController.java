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
public class PiracyController {

    /*
    FIXME 01: ¿La separacion en capas esta bien hecha? Tenemos dos 'domain'. Uno en persistence y otro en services
    */
    /*
     FIXME 01: Configurar bien Jacoco o bien Cobertura para que me indique la cobertura de los mas de 50 test que tengo
     */
    /*
     * FIXME 02: Atender a los Bugs de Sonar y revisar el sonar
     */
    /*
	 * TODO 01: Meter las licencias y el plugin de licencias: mvn license
	 * (https://choosealicense.com/licenses/mit/)
	 */
    /** The Constant LOGGER. */
    // LOGGER
    private static final Logger LOGGER 			= LoggerFactory.getLogger(PiracyController.class);

    /** The Constant WELCOME_TEXT. */
    private static final String	WELCOME_TEXT   	= "Video websites scaper (VWS) is available!";
    //
    // Area de datos
    /** The max billboard films. */
    //
    @Value("${general.scraping.parse.maxbillboardfilms}")
    public int 						maxBillboardFilms;

    /** The max video premieres. */
    @Value("${general.scraping.parse.maxvideopremieres}")
    public int 						maxVideoPremieres;

    /** The max T vshows. */
    @Value("${general.scraping.parse.maxtvshows}")
    public int 						maxTVshows;

    /** The web torrent spider. */
    @Autowired
    private final WebTorrentSpider 		webTorrentSpider;

    /**
     * Constructor.
     *
     * @param webTorrentSpider web torrent spider service
     */
    public PiracyController(final WebTorrentSpider webTorrentSpider) {
        super();
        this.webTorrentSpider = webTorrentSpider;
    }

    /**
     * Hello.
     *
     * @return One text if this web site is available
     */
    @GetMapping(value="/")
    String hello() {
        LOGGER.info(WELCOME_TEXT);
        return WELCOME_TEXT;
    }

    /**
     * Parse the torrent portal for 'scraping' the billboard.
     *
     * @return JSon object, with the billboard films in the torrent portal [0, maxBillboardFilms]
     */
    @GetMapping(value="/billboardfilms/")
    public ResponseEntity<?> parseBillBoardFilms() {
        LOGGER.info("PiracyController - Getting billboard films ...");
        final Set<Show> shows = this.webTorrentSpider.parseBillboardFilms(this.maxBillboardFilms);
        if ( shows.isEmpty()) {
            LOGGER.warn("PiracyController - Billbaord films not found.");
            return new ResponseEntity<>(new CustomErrorType(
                    "Billboard films list is empty"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shows, HttpStatus.OK);
    }

    /**
     * Parse the torrent portal for 'scraping' the video premieres.
     *
     * @return JSon object, with the video premieres in the torrent portal [0, maxSize]
     */
    @GetMapping(value="/videopremieres/")
    public ResponseEntity<?> parseVideoPremieres() {
        LOGGER.info("PiracyController - Getting video premieres ...");
        final Set<Show> shows = this.webTorrentSpider.parseVideoPremieres(this.maxVideoPremieres);
        if ( shows.isEmpty()) {
            LOGGER.warn("PiracyController - Video premieres not found.");

            return new ResponseEntity<>(new CustomErrorType(
                    "Video premieres list is empty"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shows, HttpStatus.OK);
    }

    /**
     * Parse one TV show from torrent portal, to get the last tv shows from the session.
     *
     * @param title the title
     * @return JSon object, with last episodes from TV show between '0 and maxTVshows'
     */
    @GetMapping(value="/tvshows/{title}")
    public ResponseEntity<?> parseTVShow(@PathVariable("title") final String title) {
        LOGGER.info("PiracyController - Getting the tvshow '{}'", title);
        final Set<Show> shows =  this.webTorrentSpider.parseTVShow(title, this.maxTVshows);

        if ( shows.isEmpty()) {
            LOGGER.warn("PiracyController - TVShows episodes with title '{}' not found.", title);

            return new ResponseEntity<>(new CustomErrorType(
                    "TVShows list is empty"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(shows, HttpStatus.OK);
    }
}

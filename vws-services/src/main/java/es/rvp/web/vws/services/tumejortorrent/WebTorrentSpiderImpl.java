package es.rvp.web.vws.services.tumejortorrent;

import java.util.LinkedHashSet;
import java.util.Set;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import es.rvp.web.vws.components.jsoup.JSoupHelper;
import es.rvp.web.vws.domain.Show;
import es.rvp.web.vws.domain.ShowFactory;
import es.rvp.web.vws.services.WebTorrentSpider;
/**
 * HTML Service scraping for the torrent portal 'http://tumejortorrent.com'
 *
 * @author Rodrigo Villamil Perez
 */
@Service("webTorrentSpider")
@ConfigurationProperties(prefix="general")
public class WebTorrentSpiderImpl implements WebTorrentSpider {

    /** The Constant LOGGER. */
    // LOGGER
    private static final Logger LOGGER 	= LoggerFactory.getLogger(WebTorrentSpiderImpl.class);

    /** The portal name. */
    // Constantes
    private final String PORTAL_NAME 		= "tumejortorrent.com";
    
    /** The url base. */
    private final String URL_BASE 			= "http://tumejortorrent.com";

    //
    // Area de datos
    /** The request delay. */
    //
    @Value("${general.scraping.parsertumejortorrent.requestdelay}")
    private int 						requestDelay;	// Propiedades externalizada
    //
    // IoC
    /** The j soup helper. */
    //
    @Autowired
    private final JSoupHelper 			jSoupHelper;
    
    /** The show factory. */
    @Autowired
    private final ShowFactory 			showFactory;

    /**
     * Default builder.
     *
     * @param jSoupHelper The HTML parser helper
     * @param showFactory The show factory object
     */
    public WebTorrentSpiderImpl( final JSoupHelper  	jSoupHelper,
            final ShowFactory 		showFactory) {
        this.jSoupHelper	= jSoupHelper;
        this.showFactory 	= showFactory;
    }

    /* (non-Javadoc)
     * @see es.rvp.web.vws.components.parsers.WebTorrentSpider#parseHTMLFrom(java.lang.String)
     */
    @Override
    public Show parseHTMLFrom(final String urlWithShow) {
        Show show = null;
        if (! urlWithShow.toLowerCase().contains(this.PORTAL_NAME.toLowerCase()) ) {
            LOGGER.warn(String.format("URL '%s' doesn't contains the string '%s'. The object 'Show 'returned is null",
                    urlWithShow, this.URL_BASE) );
        } else {
            final Document document = this.jSoupHelper.newInstanceByURL (urlWithShow);
            if (document != null) {
                show = this.showFactory.newInstance(urlWithShow, document.html());
            }
        }

        return show;
    }

    /* (non-Javadoc)
     * @see es.rvp.web.vws.services.WebTorrentSpider#parseBillboardFilms(int)
     */
    @Override
    public Set<Show> parseBillboardFilms(final int maxSize) {
        return this.parseShows( "/estrenos-de-cine",
                maxSize,
                "pelilist");
    }

    /* (non-Javadoc)
     * @see es.rvp.web.vws.services.WebTorrentSpider#parseVideoPremieres(int)
     */
    @Override
    public Set<Show> parseVideoPremieres(final int maxSize) {
        int overMaxSizeHD 		= maxSize/2;
        final int overMaxSizeNormal 	= maxSize/2;
        if (maxSize % 2 != 0 ){ // Si es impar ..
            overMaxSizeHD = maxSize/2 +1;
        }
        // e.g.: maxsize 0=5 --> 3 de HD y 2 de normal
        final Set<Show> showsHD = this.parseShows(
                "/peliculas-x264-mkv",
                overMaxSizeHD,
                "pelilist");
        final Set<Show> shows = this.parseShows( "/peliculas",
                overMaxSizeNormal,
                "pelilist");

        // Ojo que si hay repetidos los omite y el tamanio puede salir menor de lo esperado
        showsHD.addAll(shows);

        return showsHD;
    }

    /* (non-Javadoc)
     * @see es.rvp.web.vws.services.WebTorrentSpider#parseTVShow(java.lang.String, int)
     */
    @Override
    public Set<Show> parseTVShow(final String tvShowPath, final int maxSize) {

        return this.parseShows(  "/series-hd/" + tvShowPath,
                                 maxSize,
                                "buscar-list");
    }


    /**
     * Parses the shows.
     *
     * @param urlPath the url path
     * @param maxSize the max size
     * @param classListName the class list name
     * @return the sets the
     */
    // ----------------------- Metodos privados -------------------------------
    private Set<Show> parseShows ( final String 	urlPath,
            final int 		maxSize,
            final String 	classListName ) {

        LOGGER.info("parsing shows from {}. The max size from show list is {}", urlPath, maxSize);
        final Set<Show> shows 	= new LinkedHashSet<>();
        final Document document = this.jSoupHelper.newInstanceByURL(
                this.URL_BASE + urlPath);

        if ( document != null ) {
            // Seleccionamos todos los elemtos de la lista <li> ... </li> de nombre 'classListName'
            final Elements elements = this.jSoupHelper.selectElementsByClassListName ( document,
                    classListName );
            int idx=0;
            // menor=(x<y)?x:y;
            int sizeLimit = maxSize <= elements.size() ? maxSize : elements.size();
            LOGGER.debug(String.format(
                    "WebTorrentSpiderImpl - parseShows - Path: %s, maxSize: %s, Request Delay (ms): %s",
                    urlPath,
                    maxSize,
                    this.requestDelay));
            while (idx < sizeLimit )
            {
                try {
                    Thread.sleep(this.requestDelay);
                    final Document documentWithHref = this.jSoupHelper.newInstanceFromElementWithURL(elements.get(idx));
                    if ( documentWithHref != null) {
                        final Show show 	  = this.showFactory.newInstance( documentWithHref.baseUri(),
                                documentWithHref.html() );
                        if (show!=null) {
                            if ( shows.add(show) ) {
                                LOGGER.info(String.format(
                                        "parseShows - adding show '%s', number '%s'",
                                        show.getTitle(),
                                        idx+1));
                            } else {
                                LOGGER.info(String.format(
                                        "parseShows - Show '%s' (number '%s') already exist!. Not adding!",
                                        show.getTitle(),
                                        idx+1));
                                if (sizeLimit < elements.size()) {
                                    ++sizeLimit; // Incrementamos el tamanio para intentar meter otro que no esta repetido
                                }
                            }
                        }
                    }

                    idx++;

                } catch (final InterruptedException e) {
                    LOGGER.error(e.getMessage(), e.getCause());
                }
            }
        }

        LOGGER.info("parseShows - Return {} shows", shows.size());


        return shows;
    }
}

package es.rvp.web.vws.services;

import java.util.Set;

import es.rvp.web.vws.domain.Show;

/**
 * HTML Service scraping for torrent portal.
 *
 * @author Rodrigo Villamil Perez
 */
public interface WebTorrentSpider {

	/**
	 * Create new Show object from url. The URL is a link whith the show info
	 * 	e.g: http://tumejortorrent.com/descargar-pelicula/la-fiesta-de-las-salchichas/blurayrip-ac3-5-1/
	 *
	 * @param urlWithShow URL string with torrent show information
	 * @return One Show object o null
	 */
	 Show parseHTMLFrom ( String urlWithShow );

	/**
	 * Parse the torrent portal for 'scraping' the billboard.
	 *
	 * @param maxSize the max size
	 * @return A set of Show objects, with the billboard films in the torrent portal [0, maxSize]
	 */
	 Set<Show> parseBillboardFilms ( final int maxSize );

	/**
	 * Parse the torrent portal for 'scraping' the video premieres.
	 *
	 * @param maxSize the max size
	 * @return A set of Show objects, with the video premieres in the torrent portal [0, maxSize]
	 */
	 Set<Show> parseVideoPremieres ( final int maxSize );

	/**
	 * Parse one TV show from torrent portal, to get the last tv shows from the session.
	 *
	 * @param tvShowPath the tv show path
	 * @param maxSize the max size
	 * @return The last episodes from TV show between '0 and maxSize'
	 */
	 Set<Show> parseTVShow ( final String tvShowPath,
			 				 final int maxSize );
}

package es.rvp.web.vws.services;

import java.util.Set;

import es.rvp.web.vws.domain.Show;

/**
 * @author Rodrigo Villamil Pérez
 */
public interface ShowFavoritesService {
	/**
	 * @return a Set of shows with my favorites shows
	 */
	Set<Show> myFavorites();
}

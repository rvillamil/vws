package es.rvp.web.vws.resources.controllers;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

import es.rvp.web.vws.domain.Favorite;

/**
 * @RepositoryRestResource is not required for a repository to be exported. It
 *                         is only used to change the export details, such as
 *                         using /favorites instead of the default value of
 *                         /favorites.
 * @author Rodrigo Villamil Pérez
 */
@CrossOrigin(origins = "http://localhost:9090")
@RepositoryRestResource(collectionResourceRel = "favorites", path = "favorites")
public interface ShowFavoritesRestRepository extends PagingAndSortingRepository<Favorite, Long> {

	// add : curl -i -X POST -H "Content-Type:application/json" -d "{  \"title\" : \"Frodo2\" }" http://localhost:8080/favorites

	Favorite findByTitle(@Param("title") String title);
}

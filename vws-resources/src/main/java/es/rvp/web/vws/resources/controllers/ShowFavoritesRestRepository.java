package es.rvp.web.vws.resources.controllers;

import java.util.Set;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import es.rvp.web.vws.domain.Show;

/**
 * @RepositoryRestResource is not required for a repository to be exported. It
 *                         is only used to change the export details, such as
 *                         using /favorites2 instead of the default value of
 *                         /shows.
 * @author Rodrigo Villamil Pérez
 */
@RepositoryRestResource(collectionResourceRel = "favorites", path = "favorites")
public interface ShowFavoritesRestRepository extends PagingAndSortingRepository<Show, Long> {

	Set<Show> findByTitle(@Param("title") String title);
}

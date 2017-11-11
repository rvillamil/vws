package es.rvp.web.vws.domain;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

/**
 * This will be AUTO IMPLEMENTED by Spring into a Bean called favoriteRepository
 * CRUD refers Create, Read, Update, Delete
 *
 * @author Rodrigo Villamil Pérez
 */
@Repository
public interface FavoriteRepository extends CrudRepository<Favorite, Long> {

	// SELECT f from Favorite f WHERE f.account.username = :username
	Collection<Favorite> findByAccountUserName(String userName);

	// SELECT f from Favorite f WHERE f.account.username = :username AND f.title = :title
	Optional<Favorite> findByAccountUserNameAndTitle(String userName, String title);
}


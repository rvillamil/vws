package es.rvp.web.vws.domain;

import java.util.Collection;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// TODO: Auto-generated Javadoc
/**
 * This will be AUTO IMPLEMENTED by Spring into a Bean called favoriteRepository
 * CRUD refers Create, Read, Update, Delete.
 *
 * @author Rodrigo Villamil Pérez
 */
@Repository
public interface FavoriteRepository extends CrudRepository<Favorite, Long> {

	/**
	 * Find by account user name.
	 *
	 * @param userName the user name
	 * @return the collection
	 */
	// SELECT f from Favorite f WHERE f.account.username = :username
	Collection<Favorite> findByAccountUserName(String userName);

	/**
	 * Find by account user name and title.
	 *
	 * @param userName the user name
	 * @param title the title
	 * @return the optional
	 */
	// SELECT f from Favorite f WHERE f.account.username = :username AND f.title = :title
	Optional<Favorite> findByAccountUserNameAndTitle(String userName, String title);
}


/**
 * Created by Rodrigo
 *
 * 21:31:51
 */
package es.rvp.web.vws.domain;

import org.springframework.data.repository.CrudRepository;

/**
 * This will be AUTO IMPLEMENTED by Spring into a Bean called favoriteRepository
 * CRUD refers Create, Read, Update, Delete
 *
 * @author Rodrigo Villamil Pérez
 */
public interface FavoriteRepository extends CrudRepository<Favorite, String> {
}

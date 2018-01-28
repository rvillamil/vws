package es.rvp.web.vws.domain;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

// TODO: Auto-generated Javadoc
/**
 * This will be AUTO IMPLEMENTED by Spring into a Bean called accountRepository
 * CRUD refers Create, Read, Update, Delete.
 *
 * @author Rodrigo Villamil Pérez
 */
@Repository
public interface AccountRepository extends CrudRepository<Account, Long> {
	
	/**
	 * Find by user name.
	 *
	 * @param userName the user name
	 * @return the optional
	 */
	// select a from Account a where a.userName = :userName
	Optional<Account> findByUserName(String userName);
}
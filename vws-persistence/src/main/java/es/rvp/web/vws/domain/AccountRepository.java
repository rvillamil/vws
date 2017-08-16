package es.rvp.web.vws.domain;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

/**
 * This will be AUTO IMPLEMENTED by Spring into a Bean called accountRepository
 * CRUD refers Create, Read, Update, Delete
 *
 * @author Rodrigo Villamil Pérez
 */
public interface AccountRepository extends CrudRepository<Account, Long> {
	// select a from Account a where a.username = :username
	Optional<Account> findByUserName(String userName);
}
package es.rvp.web.vws.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Integration test showing the basic usage of {@link AccountRepository}.
 * 
 * @RunWith(SpringRunner.class) is used to provide a bridge between Spring Boot
 *                              test features and JUnit. Whenever we are using
 *                              any Spring Boot testing features in out JUnit
 *                              tests, this annotation will be required.
 * @DataJpaTest provides some standard setup needed for testing the persistence
 *              layer: - configuring H2, an in-memory database - setting
 *              Hibernate, Spring Data, and the DataSource - performing
 *              an @EntityScan - turning on SQL logging
 * @see http://www.baeldung.com/spring-boot-testing
 * 
 * @author Rodrigo Villamil Pérez
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class AccountRepositoryIT {

	/**
	 * To carry out some DB operation, we need some records already setup in our
	 * database. To setup such data, we can use TestEntityManager. The
	 * TestEntityManager provided by Spring Boot is an alternative to the
	 * standard JPA EntityManager that provides methods commonly used when
	 * writing tests.
	 */
	@Autowired
	private TestEntityManager entityManager;

	@Autowired
	AccountRepository accountRepository;

	@Test
	public void givenExistingUserWhenFindByUserNameThenExists() {		
		// Given
		String userName = "user";
		Account account = new Account(userName, "password");
		entityManager.persist(account);
		entityManager.flush();
		
		// When
		Optional<Account> theAccount = accountRepository.findByUserName(userName);
		
		// then
		assertEquals(theAccount.get().getUserName(), account.getUserName());
	}
	
	@Test
	public void givenNotExistingUserWhenFindByUserNameThenExists() {		
		// Given		
		Account account = new Account("user", "password");
		entityManager.persist(account);
		entityManager.flush();
		
		// When
		Optional<Account> theAccount = accountRepository.findByUserName("userNone");
		
		// then
		assertFalse(theAccount.isPresent());		
	}
}

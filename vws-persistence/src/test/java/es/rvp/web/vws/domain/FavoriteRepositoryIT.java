package es.rvp.web.vws.domain;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Collection;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

// TODO: Auto-generated Javadoc
/**
 * Integration test showing the basic usage of {@link FavoriteRepository}.
 *
 * @author Rodrigo Villamil Pérez
 * @RunWith(SpringRunner.class) is used to provide a bridge between Spring Boot
 *                              test features and JUnit. Whenever we are using
 *                              any Spring Boot testing features in out JUnit
 *                              tests, this annotation will be required.
 * @DataJpaTest provides some standard setup needed for testing the persistence
 *              layer: - configuring H2, an in-memory database - setting
 *              Hibernate, Spring Data, and the DataSource - performing
 *              an @EntityScan - turning on SQL logging
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class FavoriteRepositoryIT {

	/**
	 * To carry out some DB operation, we need some records already setup in our
	 * database. To setup such data, we can use TestEntityManager. The
	 * TestEntityManager provided by Spring Boot is an alternative to the
	 * standard JPA EntityManager that provides methods commonly used when
	 * writing tests.
	 */
	@Autowired
	private TestEntityManager entityManager;

	/** The favorite repository. */
	@Autowired
	FavoriteRepository favoriteRepository;
	
	/**
	 * Given user when find favorites the renturn all favorites.
	 */
	@Test	
	public void givenUserWhenFindFavoritesTheRenturnAllFavorites() {		
				
		// Given
		String userName = "user";
		Account account = new Account(userName, "password");
		entityManager.persist(account);
		entityManager.flush();
				
		Favorite favorite1 = new Favorite (account, "favorito1");
		Favorite favorite2 = new Favorite (account, "favorito2");
		entityManager.persist(favorite1);
		entityManager.persist(favorite2);
		entityManager.flush();
		
		// When
		Collection<Favorite> favorites = favoriteRepository.findByAccountUserName(userName);
				
		// then		
		assertEquals(favorites.size(), 2);	
	}	
	
	/**
	 * Given user when find favorites then renturn empty list.
	 */
	@Test	
	public void givenUserWhenFindFavoritesThenRenturnEmptyList() {		
				
		// Given
		String userName = "user";
		Account account = new Account(userName, "password");
		entityManager.persist(account);
		entityManager.flush();
				
		
		// When
		Collection<Favorite> favorites = favoriteRepository.findByAccountUserName(userName);
				
		// then
		assertTrue(favorites.isEmpty());
		assertEquals(favorites.size(), 0);	
	}
	
	/**
	 * Given user not exist when find favorites then return empty list.
	 */
	@Test	
	public void givenUserNotExistWhenFindFavoritesThenReturnEmptyList() {		
				
		// Given
		String userName = "user";
		Account account = new Account(userName, "password");
		entityManager.persist(account);
		entityManager.flush();
				
		
		// When
		Collection<Favorite> favorites = favoriteRepository.findByAccountUserName("notExistUser");
				
		// then
		assertTrue(favorites.isEmpty());
		assertEquals(favorites.size(), 0);	
	}
	
	/**
	 * Given user name and title when find by user name and tittle then return favorite.
	 */
	@Test	
	public void givenUserNameAndTitleWhenFindByUserNameAndTittleThenReturnFavorite (){
		
		// Given
		String userName = "user";
		Account account = new Account(userName, "password");
		entityManager.persist(account);
		entityManager.flush();
		
		Favorite favorite1 = new Favorite (account, "title1");		
		entityManager.persist(favorite1);		
		entityManager.flush();
		
		// When
		Optional<Favorite> favorite  
					= favoriteRepository.findByAccountUserNameAndTitle(userName, "title1");
		
		// then
		
		assertTrue( favorite.isPresent());
		assertEquals( favorite.get().getTitle(),"title1");
	}
	

	/**
	 * Given user name and title when find by user name and wrong tittle then return none.
	 */
	@Test	
	public void givenUserNameAndTitleWhenFindByUserNameAndWrongTittleThenReturnNone (){
		
		// Given
		String userName = "user";
		Account account = new Account(userName, "password");
		entityManager.persist(account);
		entityManager.flush();
		
		Favorite favorite1 = new Favorite (account, "title1");		
		entityManager.persist(favorite1);		
		entityManager.flush();
		
		// When
		Optional<Favorite> favorite  
					= favoriteRepository.findByAccountUserNameAndTitle(userName, "no_title");
		
		// then		
		assertFalse(  favorite.isPresent());		
	}
	
	
	/**
	 * Given user name and title when find by wrong user name and tittle then return none.
	 */
	@Test	
	public void givenUserNameAndTitleWhenFindByWrongUserNameAndTittleThenReturnNone (){
		
		// Given
		String userName = "user";
		Account account = new Account(userName, "password");
		entityManager.persist(account);
		entityManager.flush();
		
		Favorite favorite1 = new Favorite (account, "title1");		
		entityManager.persist(favorite1);		
		entityManager.flush();
		
		// When
		Optional<Favorite> favorite  
					= favoriteRepository.findByAccountUserNameAndTitle("wrongUsername", "title1");
		
		// then		
		assertFalse(  favorite.isPresent());		
	}
	
	
	/**
	 * Given user name and title when find by wrong user name and wrong tittle then return none.
	 */
	@Test	
	public void givenUserNameAndTitleWhenFindByWrongUserNameAndWrongTittleThenReturnNone (){
		
		// Given
		String userName = "user";
		Account account = new Account(userName, "password");
		entityManager.persist(account);
		entityManager.flush();
		
		Favorite favorite1 = new Favorite (account, "title1");		
		entityManager.persist(favorite1);		
		entityManager.flush();
		
		// When
		Optional<Favorite> favorite  
					= favoriteRepository.findByAccountUserNameAndTitle("wrongUsername", "wrongtitle");
		
		// then		
		assertFalse(  favorite.isPresent());		
	}

}

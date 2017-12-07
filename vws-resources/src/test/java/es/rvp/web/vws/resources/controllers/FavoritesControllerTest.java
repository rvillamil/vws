package es.rvp.web.vws.resources.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.Collection;
import java.util.LinkedList;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import es.rvp.web.vws.domain.Account;
import es.rvp.web.vws.domain.AccountRepository;
import es.rvp.web.vws.domain.Favorite;
import es.rvp.web.vws.domain.FavoriteRepository;

/**
 * To test the Controllers, we can use @WebMvc	Test. It will auto-configure the Spring
 * MVC infrastructure for our unit tests.
 * In most of the cases, @WebMvcTest will be limited to bootstrap a single controller.
 * It is used along with @MockBean to provide mock implementations for required dependencies.
 *
 * @author Rodrigo Villamil Perez
 */
@RunWith(SpringRunner.class)
@WebMvcTest(FavoritesController.class)
public class FavoritesControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockBean
    private  FavoriteRepository 	favoriteRepository;
    @MockBean
    private  AccountRepository 		accountRepository;


    // Test Data
    private final String userNameTest = "user";
    private final String userNamePassword = "password";
    private Account account;
    private Optional<Account> nonEmptyAccount ;
    private Principal mockPrincipal;

    @Before
    public void setup(){
    	this.account = new Account (this.userNameTest, this.userNamePassword);
    	this.nonEmptyAccount = Optional.of( this.account );
    	this.mockPrincipal = mock(Principal.class);
    	when (this.mockPrincipal.getName()).thenReturn(this.userNameTest);
    	when (this.accountRepository.findByUserName(
    		  this.mockPrincipal.getName())).thenReturn(this.nonEmptyAccount);
    }

    // ----------------------- listAllFavorites ---------------------------
    @Test
    public void givenAccountWithFavorites_whenGetAllFavorites_thenReturnJsonArray() throws Exception {
    	// Given
    	final Collection<Favorite> favorites = this.createFavoritesToTest (this.account, 3, "Lost");

    	// When - Then
    	when (this.favoriteRepository.findByAccountUserName(
      		  this.mockPrincipal.getName())).thenReturn(favorites);

    	this.mvc.perform 	( get("/api/favorites/")
    			 .principal(this.mockPrincipal)
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$", hasSize(3)))
                 .andExpect(jsonPath("$[0].title", is("Lost_0")))
                 .andExpect(jsonPath("$[1].title", is("Lost_1")))
                 .andExpect(jsonPath("$[2].title", is("Lost_2")));
    }

    @Test
    public void givenAccountWithOuthFavorites_whenGetAllFavorites_thenReturnEpmtyArray() throws Exception {
    	// Given
    	final Collection<Favorite> favorites = this.createFavoritesToTest (this.account, 0, "Lost");

    	// When - Then
    	when (this.favoriteRepository.findByAccountUserName(
      		  this.mockPrincipal.getName())).thenReturn(favorites);

    	this.mvc.perform 	( get("/api/favorites/")
    			 .principal(this.mockPrincipal)
                 .contentType(MediaType.APPLICATION_JSON))
    			 .andExpect(jsonPath("$.errorMessage", is("Favorite list is empty")))
    			 .andExpect(status().is4xxClientError() );
    }

    // ------------------------ getFavorite -----------------------------
    @Test
    public void givenTitle_whenGetFavorite_thenReturnJson() throws Exception {
    	// Given
    	final Favorite favorite = new Favorite (this.account, "Lost");

    	// When - Then
    	when (this.favoriteRepository.findByAccountUserNameAndTitle(
        		  this.mockPrincipal.getName(), "Lost")).thenReturn(
        				  Optional.of(favorite));

    	this.mvc.perform 	( get("/api/favorites/Lost")
   			 	.principal(this.mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk());
    }

    @Test
    public void givenNotExistingTitle_whenGetFavorite_thenReturnJson() throws Exception {
    	// Given
    	// When - Then

    	final Favorite favorite = new Favorite (this.account, "Lost");

    	// When - Then
    	when (this.favoriteRepository.findByAccountUserNameAndTitle(
        		  this.mockPrincipal.getName(), "Lost")).thenReturn(
        				  Optional.of(favorite));

    	this.mvc.perform 	( get("/api/favorites/none")
   			 	.principal(this.mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().is4xxClientError());
    }

    // ----------------------- createFavorite ---------------------------
    // ----------------------- updateFavorite ---------------------------
    // ----------------------- deleteFavorite ---------------------------


    //------------------------ Helpers Methods --------------------------
    private Collection<Favorite> createFavoritesToTest ( final Account account ,
    													 final int numFavorites,
    													 final String prefixName) {
    	final Collection<Favorite> favorites = new LinkedList<> ();
    	for (int i = 0; i < numFavorites; i++) {
    		favorites.add(  new Favorite (this.account, prefixName + "_" + i));
        }
    	return favorites;
    }
}

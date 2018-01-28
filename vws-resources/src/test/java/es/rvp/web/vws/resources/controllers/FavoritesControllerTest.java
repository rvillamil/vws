package es.rvp.web.vws.resources.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
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

// TODO: Auto-generated Javadoc
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

    /** The mvc. */
    @Autowired
    private MockMvc mvc;

    /** The favorite repository. */
    @MockBean
    private  FavoriteRepository 	favoriteRepository;
    
    /** The account repository. */
    @MockBean
    private  AccountRepository 		accountRepository;


    /** The user name test. */
    // Test Data
    private final String userNameTest = "user";
    
    /** The user name password. */
    private final String userNamePassword = "password";
    
    /** The account. */
    private Account account;
    
    /** The non empty account. */
    private Optional<Account> nonEmptyAccount ;
    
    /** The mock principal. */
    private Principal mockPrincipal;

    /**
     * Setup.
     */
    @Before
    public void setup(){
    	this.account = new Account (this.userNameTest, this.userNamePassword);
    	this.nonEmptyAccount = Optional.of( this.account );
    	this.mockPrincipal = mock(Principal.class);
    	when (this.mockPrincipal.getName()).thenReturn(this.userNameTest);
    	when (this.accountRepository.findByUserName(
    		  this.mockPrincipal.getName())).thenReturn(this.nonEmptyAccount);
    }

    /**
     * Given account with favorites when get all favorites then return json array.
     *
     * @throws Exception the exception
     */
    // ----------------------- listAllFavorites ---------------------------
    @Test
    public void givenAccountWithFavorites_whenGetAllFavorites_thenReturnJsonArray() throws Exception {
    	// Given
    	final Collection<Favorite> favorites = this.createFavoritesToTest (this.account, 3, "Lost");

    	when (this.favoriteRepository.findByAccountUserName(
      		  this.mockPrincipal.getName())).thenReturn(favorites);

    	// When - Then
    	this.mvc.perform 	( get("/api/favorites/")
    			 .principal(this.mockPrincipal)
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$", hasSize(3)))
                 .andExpect(jsonPath("$[0].title", is("Lost_0")))
                 .andExpect(jsonPath("$[1].title", is("Lost_1")))
                 .andExpect(jsonPath("$[2].title", is("Lost_2")));
    }

    /**
     * Given account with outh favorites when get all favorites then return epmty array.
     *
     * @throws Exception the exception
     */
    @Test
    public void givenAccountWithOuthFavorites_whenGetAllFavorites_thenReturnEpmtyArray() throws Exception {
    	// Given
    	final Collection<Favorite> favorites = this.createFavoritesToTest (this.account, 0, "Lost");

    	when (this.favoriteRepository.findByAccountUserName(
      		  this.mockPrincipal.getName())).thenReturn(favorites);

    	// When - Then
    	this.mvc.perform 	( get("/api/favorites/")
    			 .principal(this.mockPrincipal)
                 .contentType(MediaType.APPLICATION_JSON))
    			 .andExpect(jsonPath("$.errorMessage", is("Favorite list is empty")))
    			 .andExpect(status().is4xxClientError() );
    }

    /**
     * Given title when get favorite then return json.
     *
     * @throws Exception the exception
     */
    // ------------------------ getFavorite -----------------------------
    @Test
    public void givenTitle_whenGetFavorite_thenReturnJson() throws Exception {
    	// Given
    	final Favorite favorite = new Favorite (this.account, "Lost");
    	when (this.favoriteRepository.findByAccountUserNameAndTitle(
        		  this.mockPrincipal.getName(), "Lost")).thenReturn(
        				  Optional.of(favorite));
    	// When - Then
    	this.mvc.perform 	( get("/api/favorites/Lost")
   			 	.principal(this.mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().isOk());
    }

    /**
     * Given not existing title when get favorite then return json.
     *
     * @throws Exception the exception
     */
    @Test
    public void givenNotExistingTitle_whenGetFavorite_thenReturnJson() throws Exception {
    	// Given
    	final Optional<Favorite> favorite = Optional.empty();
    	when (this.favoriteRepository.findByAccountUserNameAndTitle(
        		  this.mockPrincipal.getName(), "none")).thenReturn(
        				  favorite);

    	// When - Then
    	this.mvc.perform 	( get("/api/favorites/none")
   			 	.principal(this.mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON))
    			.andExpect(status().is4xxClientError());
    }

    /**
     * Given not existing favorite when create new favorite then return json.
     *
     * @throws Exception the exception
     */
    // ----------------------- createFavorite ---------------------------
    @Test
    public void givenNotExistingFavorite_whenCreateNewFavorite_thenReturnJson() throws Exception {

    	// Given
    	final Optional<Favorite> favorite = Optional.of(new Favorite (this.account, "Lost"));
    	when (this.favoriteRepository.findByAccountUserNameAndTitle(
        		  this.mockPrincipal.getName(), "Lost")).thenReturn(
        				  Optional.empty());
    	when (this.favoriteRepository.save(
    			any(Favorite.class))).thenReturn ( favorite.get());

    	// When - Then
    	this.mvc.perform  ( post("/api/favorites/" )
   			 	.principal(this.mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestSupport.toJson(favorite.get())))
    			.andDo(print())
    			.andExpect(status().is2xxSuccessful());

    }

    /**
     * Given existing favorite when create new favorite then return HTTP 4 XX.
     *
     * @throws Exception the exception
     */
    @Test
    public void givenExistingFavorite_whenCreateNewFavorite_thenReturnHTTP4XX () throws Exception {
    	// Given
    	final Optional<Favorite> favorite = Optional.of(new Favorite (this.account, "Lost"));

    	when (this.favoriteRepository.findByAccountUserNameAndTitle(
        		  this.mockPrincipal.getName(), "Lost")).thenReturn(
        				  favorite);
    	// When - Then --> Fuerza a que exista el favorito
    	this.mvc.perform  ( post("/api/favorites/" )
   			 	.principal(this.mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestSupport.toJson(favorite.get())))
    			.andDo(print())
    			.andExpect(status().is4xxClientError());
    }

    /**
     * Given existing favorite when update then return json.
     *
     * @throws Exception the exception
     */
    // ----------------------- updateFavorite ---------------------------
    @Test
    public void givenExistingFavorite_whenUpdate_thenReturnJson() throws Exception {
    	// Given
    	final Long 		id 			= 5L;
    	final Favorite  newFavorite = new Favorite (this.account, "New Favorite");

    	final Optional<Favorite> favoriteForUpdate = Optional.of(new Favorite (
    			this.account, "Favorite for update"));
    	when (this.favoriteRepository.findOne(id)).thenReturn(
    			favoriteForUpdate.get());

    	// When - Then
    	this.mvc.perform  ( put("/api/favorites/5" )
   			 	.principal(this.mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestSupport.toJson(newFavorite)))
    			.andExpect(jsonPath("$.title", is("New Favorite")))
    			.andDo(print())
    			.andExpect(status().is2xxSuccessful());
    }

    /**
     * Given not existing favorite when update then return HTTP 4 XX.
     *
     * @throws Exception the exception
     */
    @Test
    public void givenNotExistingFavorite_whenUpdate_thenReturnHTTP4XX() throws Exception {
    	// Given
    	final Long 		id 			= 5L;
    	final Favorite  newFavorite = new Favorite (this.account, "New Favorite");

    	when (this.favoriteRepository.findOne(id)).thenReturn(null);

    	// When - Then
    	this.mvc.perform  ( put("/api/favorites/5" )
   			 	.principal(this.mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON)
                .content(TestSupport.toJson(newFavorite)))
    			.andDo(print())
    			.andExpect(status().is4xxClientError());
    }

    /**
     * Given existing favorite when delete then return HTTP 2 XX.
     *
     * @throws Exception the exception
     */
    // ----------------------- deleteFavorite ---------------------------
    @Test
    public void givenExistingFavorite_whenDelete_thenReturnHTTP2XX() throws Exception {
    	// Given
    	final Long 		id 			= 5L;
    	final Optional<Favorite> favoriteToDelete = Optional.of(new Favorite (
    			this.account, "Favorite to delete"));
    	when (this.favoriteRepository.findOne(id)).thenReturn(favoriteToDelete.get());

    	// When - Then
    	this.mvc.perform  ( delete("/api/favorites/5" )
   			 	.principal(this.mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON))
    			.andDo(print())
    			.andExpect(status().is2xxSuccessful());
    }

    /**
     * Given not existing favorite when delete then return HTTP 4 XX.
     *
     * @throws Exception the exception
     */
    @Test
    public void givenNotExistingFavorite_whenDelete_thenReturnHTTP4XX() throws Exception {
     	// Given
    	final Long 		id 			= 5L;
    	when (this.favoriteRepository.findOne(id)).thenReturn(null);

    	// When - Then
    	this.mvc.perform  ( delete("/api/favorites/5" )
   			 	.principal(this.mockPrincipal)
                .contentType(MediaType.APPLICATION_JSON))
    			.andDo(print())
    			.andExpect(status().is4xxClientError());
    }

    /**
     * Creates the favorites to test.
     *
     * @param account the account
     * @param numFavorites the num favorites
     * @param prefixName the prefix name
     * @return the collection
     */
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

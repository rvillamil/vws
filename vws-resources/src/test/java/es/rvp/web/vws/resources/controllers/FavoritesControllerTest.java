package es.rvp.web.vws.resources.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.security.Principal;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import es.rvp.web.vws.domain.Account;
import es.rvp.web.vws.domain.AccountRepository;
import es.rvp.web.vws.domain.FavoriteRepository;
import es.rvp.web.vws.domain.Show;
import es.rvp.web.vws.services.WebTorrentSpider;

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

    @Test
    void givenAccount_whenGetAllFavorites_thenReturnJsonArray() throws Exception {
    	// Given
    	Account account = new Account("user", "password");
    	
    	// Principal principal = new Principal(); --> FIXME 00: ¿Como hago esto? ¿haiblito la seguridad? Entonces me falla el resto..
    	   	//when ( this.favoriteRepository.findByAccountUserName(principal.getName());
    	
    	// When - Then
    	this.mvc.perform 	( get("/api/favorites/")
                 .contentType(MediaType.APPLICATION_JSON))
                 .andExpect(status().isOk() )
                 .andExpect(jsonPath("$", hasSize(2)))
                 .andExpect(jsonPath("$[0].title", is("La Isla_0")))
                 .andExpect(jsonPath("$[1].title", is("La Isla_1")));
    }
}

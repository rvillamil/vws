package es.rvp.web.vws.resources.controllers;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
@WebMvcTest(PiracyController.class)
public class PiracyControllerTest {

    @Value("${general.scraping.parse.maxbillboardfilms}")
    public int 						maxBillboardFilms;
    @Value("${general.scraping.parse.maxvideopremieres}")
    public int 						maxVideoPremieres;
    @Value("${general.scraping.parse.maxtvshows}")
    public int 						maxTVshows;

    @Autowired
    private MockMvc mvc;

    @MockBean
    private WebTorrentSpider 			webTorrentSpider;

    //--------------------------- parseBillboardFilms -------------------------
    @Test
    public void givenwebtorrentwith2films_whenParseFilmsInBillBoard_ThenReturnJsonArray() throws Exception {
        // Given
        final Set<Show> shows =	this.newShowsToTest(2, "La Isla");

        // When - Then
        when(this.webTorrentSpider.parseBillboardFilms(this.maxBillboardFilms)).thenReturn(
                shows);

        this.mvc.perform 	( get("/api/billboardfilms/")
                      .contentType(MediaType.APPLICATION_JSON))
                      .andExpect(status().isOk() )
                      .andExpect(jsonPath("$", hasSize(2)))
                      .andExpect(jsonPath("$[0].title", is("La Isla_0")))
                      .andExpect(jsonPath("$[1].title", is("La Isla_1")));
    }

    @Test
    public void givenwebtorrentwith0films_whenParseFilmsInBillBoard_ThenReturnEmtpyArray() throws Exception {
        // Given
        final Set<Show> shows = new HashSet<>();

        // When - Then
        when(this.webTorrentSpider.parseBillboardFilms(this.maxBillboardFilms)).thenReturn(
                shows);

        this.mvc.perform 	( get("/api/billboardfilms/")
                      .contentType(MediaType.APPLICATION_JSON))
                      .andExpect(jsonPath("$.errorMessage", is("Billboard films list is empty")))
                      .andExpect(status().is4xxClientError() );

    }

    //--------------------------- parseVideoPremieres -------------------------
    /*
    @Test
    public void whenParseVideoPremieresThenGetTwoVideoPremieres() {
        // Given
        // When
        when(this.webTorrentSpider.parseVideoPremieres(this.piracyController.maxVideoPremieres)).thenReturn(
                this.newShowsToTest(2, "La Isla en Video"));

        final Set<Show> filmsBillboard = this.piracyController.parseVideoPremieres();
        // Then
        assertNotNull(filmsBillboard);
        assertEquals(filmsBillboard, this.newShowsToTest(2, "La Isla en Video"));
        assertTrue(filmsBillboard.size() == 2);
    }
    */
    //---------------------------- parseTVShow --------------------------------
    /*
    @Test
    public void whenParseTVShowThenGetTheLastThreeEpisodes() {
        // Given
        String tvShowPath="series-hd/modern-family/";

        // When
        when(this.webTorrentSpider.parseTVShow(tvShowPath,this.piracyController.maxTVshows)).thenReturn(
                        this.newShowsToTest(2, "Modern Family HD"));

        final Set<Show> filmsBillboard = this.piracyController.parseTVShow(tvShowPath);
        // Then
        assertNotNull(filmsBillboard);
        assertEquals(filmsBillboard, this.newShowsToTest(2, "Modern Family HD"));
        assertTrue(filmsBillboard.size() == 2);

    }
     */
    //-------------------------- Helpers Methods ------------------------------
    private Set<Show> newShowsToTest(final int numShows, final String prefixName) {
        final Set<Show> shows = new LinkedHashSet<>();

        for (int i = 0; i < numShows; i++) {
            shows.add(Show.builder().title(prefixName + "_" + i).build());
        }

        return shows;
    }
}

package es.rvp.web.vws.resources.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import es.rvp.web.vws.domain.Favorite;
import es.rvp.web.vws.domain.FavoriteRepository;

/**
 * Rest Controller In Spring’s approach to building RESTful web services, HTTP
 * requests are handled by a controller.
 *
 * @author Rodrigo Villamil Pérez
 */
@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:9090")
public class FavoritesController {
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger(FavoritesController.class);
	@Autowired
	private final FavoriteRepository favoriteRepository;

	/**
	 * Builder
	 * @param favoriteRepository DDBB Repository
	 */
	public FavoritesController(final FavoriteRepository favoriteRepository) {
		super();
		this.favoriteRepository = favoriteRepository;
	}

	// http://websystique.com/spring-boot/spring-boot-rest-api-example/
	@RequestMapping(value = "/favorites/",
					method = RequestMethod.GET)
	public ResponseEntity<?> listAllFavorites () {
		LOGGER.info("FavoritesController - listAllFavorites ...");

		Iterable<Favorite> favorites =  this.favoriteRepository.findAll();
		if (! favorites.iterator().hasNext()) {
            return new ResponseEntity<>(new CustomErrorType(
            		"Favorite list is empty"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(favorites, HttpStatus.OK);
	}


	@RequestMapping(value = "/favorites/{title}",
					method = RequestMethod.GET)
	public ResponseEntity<?> getFavorite(@PathVariable final String title) {
		LOGGER.info("FavoritesController - getFavorite with title '{}'", title);

		Favorite favorite = this.favoriteRepository.findOne(title);

		if (favorite == null) {
			LOGGER.error("Favorite with title {} not found.", title);
			return new ResponseEntity<>(new CustomErrorType(
					"Favorite with title " + title + " not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(favorite, HttpStatus.OK);
	}

	@RequestMapping(value = "/favorites/", method = RequestMethod.POST)
	public ResponseEntity<?> createFavorite(@RequestBody final String title,
											final UriComponentsBuilder ucBuilder) {
		LOGGER.info("FavoritesController - createFavorite with title '{}'", title);

		 if (this.favoriteRepository.exists(title)) {
			 LOGGER.error("Unable to create. A favorite with title {} already exist", title);
	            return new ResponseEntity<>(new CustomErrorType("Unable to create. A Favorite with title " +
	            title+ " already exist."),HttpStatus.CONFLICT); // 409
	     }
		 Favorite favorite = new Favorite();
		 favorite.setTitle(title);
		 this.favoriteRepository.save(favorite);


		 HttpHeaders headers = new HttpHeaders();
	     headers.setLocation(ucBuilder.path("/api/favorites/{title}").buildAndExpand(title).toUri());
	     return new ResponseEntity<>(headers, HttpStatus.CREATED);
	}
}

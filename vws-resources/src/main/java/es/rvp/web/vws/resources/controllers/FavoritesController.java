package es.rvp.web.vws.resources.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import es.rvp.web.vws.domain.Favorite;
import es.rvp.web.vws.domain.FavoriteRepository;

/**
 * Rest Controller In Spring’s approach to building RESTful web services, HTTP
 * requests are handled by a controller.
 *
 * @author Rodrigo Villamil Pérez
 */
@RestController
//@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:8080")
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
	@GetMapping(path = "/favorites")
	public Iterable<Favorite> getAllFavorites() {
		LOGGER.info("FavoritesController - getAllFavorites");
		// This returns a JSON or XML with the users
		return this.favoriteRepository.findAll();
	}

	//
	@RequestMapping(value = "/favorites/{title}", method = RequestMethod.GET)
	@ResponseBody
	public Favorite getFavorite(@PathVariable final String title) {
		LOGGER.info("FavoritesController - getFavorite with title '{}'", title);
		return this.favoriteRepository.findOne(title);
	}

	// Example CURL: curl -X POST --header 'Content-Type: application/json' --header 'Accept: application/json'
	@RequestMapping(value = "/favorites/{title}", method = RequestMethod.POST)
	@ResponseBody
	public Favorite addNewFavorite(@PathVariable final String title) {
		LOGGER.info("FavoritesController - postFavorite with title '{}'", title);
		Favorite favorite = new Favorite();
		favorite.setTitle(title);
		this.favoriteRepository.save(favorite);

		return favorite;
	}
}

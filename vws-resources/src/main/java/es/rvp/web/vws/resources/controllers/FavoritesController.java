package es.rvp.web.vws.resources.controllers;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import es.rvp.web.vws.domain.AccountRepository;
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
// @CrossOrigin(origins = "http://localhost:9090")
public class FavoritesController {
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger(FavoritesController.class);

	private final FavoriteRepository 	favoriteRepository;
	private final AccountRepository 	accountRepository;

	/**
	 * Builder
	 * @param favoriteRepository repository with favorites user shows
	 * @param AccountRepository  repository with user account
	 */
	@Autowired
	public FavoritesController(final FavoriteRepository favoriteRepository,
							   final AccountRepository  accountRepository) {
		super();
		this.accountRepository = accountRepository;
		this.favoriteRepository = favoriteRepository;
	}

	/**
	 * GET all favorites for user request
	 *
	 * @param userId The username
	 * @return HttpStatus.OK if favorites are found. HttpStatus.NOT_FOUND in other case
	 */
	@RequestMapping(value = "/{userId}/favorites/",
					method = RequestMethod.GET)
	public ResponseEntity<?> listAllFavorites (@PathVariable final String userId) {
		LOGGER.info("Getting all favorites for userId '{}'", userId);
		this.validateUser(userId);

		final Iterable<Favorite> favorites =  this.favoriteRepository.findByAccountUserName(userId);
		if (! favorites.iterator().hasNext() ) {
            return new ResponseEntity<>(new CustomErrorType(
            		"Favorite list is empty"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(favorites, HttpStatus.OK);
	}

	/**
	 * GET user favorite by title
	 *
	 * @param userId The username
	 * @param title The name of the favorite
	 * @return HttpStatus.OK if favorites are found. HttpStatus.NOT_FOUND in other case
	 */
	@RequestMapping(value = "/{userId}/favorites/{title}",
					method = RequestMethod.GET)
	public ResponseEntity<?> getFavorite(@PathVariable final String userId,
										 @PathVariable final String title) {
		LOGGER.info("Fetching favorite for user '{}' with title '{}'", userId, title);
		this.validateUser(userId);

		final Optional<Favorite> favorite = this.favoriteRepository.findByAccountUserNameAndTitle(userId, title);

		if (!favorite.isPresent()) {
			LOGGER.error("Favorite for user '{}' with title '{}' not found.", userId, title);
			return new ResponseEntity<>(new CustomErrorType(
					"Favorite with title '" + title + "' not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(favorite.get(), HttpStatus.OK);
	}

	/**
	 * POST favorite. Create new favorite
	 *
	 * @param userId The username
	 * @param newFavorite The favorite object to create
	 * @param ucBuilder utility to set headers info
	 * @return HttpStatus.CREATED or HttpStatus.CONFLICT if already exists
	 */
	@RequestMapping(value = "/{userId}/favorites/",
					method = RequestMethod.POST)
	public ResponseEntity<?> createFavorite( @PathVariable final String userId,
											 @RequestBody final Favorite newFavorite,
											 final UriComponentsBuilder ucBuilder) {
		LOGGER.info("POST favorite with title '{}'", newFavorite.getTitle());
		this.validateUser(userId);

		if ( this.favoriteRepository.findByAccountUserNameAndTitle(userId, newFavorite.getTitle()).isPresent() ) {
			 LOGGER.error("Unable to create. A favorite with title '{}' already exist", newFavorite.getTitle());

			 return new ResponseEntity<>(new CustomErrorType(
	        		 "Unable to create. A Favorite with title '" + newFavorite.getTitle() + "' already exist."),
	        		 HttpStatus.CONFLICT); // 409
	    }

		return this.accountRepository.findByUserName(userId)
				.map(account -> {
							final Favorite result = this.favoriteRepository.save(new Favorite(
									account,newFavorite.getTitle()));
							final HttpHeaders headers = new HttpHeaders();
							headers.setLocation(ucBuilder.path("/{id}").buildAndExpand(result.getId()).toUri());

							return new ResponseEntity<>(headers, HttpStatus.CREATED);
						}
				).orElse(ResponseEntity.noContent().build());
	}

	/**
	 * PUT (Update) the favorite by id
	 *
	 * @param id The favorite ID in database
	 * @param newFavorite Object to replace
	 * @return HttpStatus.OK or HttpStatus.NOT_FOUND if not exists
	 */
	 @RequestMapping(value = "/favorites/{id}",
			 		 method = RequestMethod.PUT)
	 public ResponseEntity<?> updateFavorite( @PathVariable final Long 		id,
			 								  @RequestBody  final Favorite  newFavorite) {

		 LOGGER.info("Updating (PUT) favorite with id '{}'", id);

		 final Favorite favoriteForUpdate = this.favoriteRepository.findOne(id);
		 if ( favoriteForUpdate == null ) {
			 LOGGER.error("Unable to update. Favorite with id '{}' not found.", id);
			 return new ResponseEntity<>(new CustomErrorType(
	            		"Unable to update. Favorite with id '" + id + "' not found."),
	                    HttpStatus.NOT_FOUND);
		 }

		 favoriteForUpdate.setTitle(newFavorite.getTitle());
		 this.favoriteRepository.save(favoriteForUpdate);
		 return new ResponseEntity<>(favoriteForUpdate, HttpStatus.OK);
	 }

	 /**
	  * DELETE Favorite by id
	  *
	  * @param id The favorite ID in database
	  * @return HttpStatus.NO_CONTENT if favorite has been deleted. HttpStatus.NOT_FOUND in other case
	  */
	 @RequestMapping ( value = "/favorites/{id}",
			 		   method = RequestMethod.DELETE)
	 public ResponseEntity<?> deleteFavorite( @PathVariable final  Long id) {
		 LOGGER.info("Fetching & Deleting favorite with id '{}'", id);

		 final Favorite favoriteForDelete = this.favoriteRepository.findOne(id);
		 if ( favoriteForDelete == null ) {
			 LOGGER.error("Unable to delete. Favorite with id '{}' not found.", id);
			 return new ResponseEntity<>(new CustomErrorType(
	            		"Unable to delete. Favorite with id '" + id + "' not found."),
	                    HttpStatus.NOT_FOUND);
		 }

		 this.favoriteRepository.delete(id);
		 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	 }

	 // ---------------------------------- Private Methods ----------------------------------------
	 private void validateUser(final String userId) {
		 this.accountRepository.findByUserName(userId).orElseThrow(
				 () -> new UserNotFoundException(userId));
	 }
}

package es.rvp.web.vws.resources.controllers;

import java.security.Principal;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
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
@RequestMapping("/api/favorites/")
public class FavoritesController {
	
	/** The Constant LOGGER. */
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger(FavoritesController.class);

	/** The favorite repository. */
	private final FavoriteRepository 	favoriteRepository;
	
	/** The account repository. */
	private final AccountRepository 	accountRepository;

	/**
	 * Builder.
	 *
	 * @param favoriteRepository repository with favorites user shows
	 * @param accountRepository the account repository
	 */
	@Autowired
	public FavoritesController ( final FavoriteRepository favoriteRepository,
							    final AccountRepository   accountRepository) {
		super();
		this.accountRepository = accountRepository;
		this.favoriteRepository = favoriteRepository;
	}

	/**
	 * GET all favorites for the authenticated user.
	 *
	 * @param principal the principal
	 * @return HttpStatus.OK if favorites are found or HttpStatus.NOT_FOUND in other case
	 */
	@GetMapping
	public ResponseEntity<?> listAllFavorites (final Principal principal) {
		LOGGER.info("Getting all favorites for userId '{}'", principal.getName());
		this.validateUser(principal);

		final Iterable<Favorite> favorites =  this.favoriteRepository.findByAccountUserName(principal.getName());
		if (! favorites.iterator().hasNext() ) {
            return new ResponseEntity<>(new CustomErrorType(
            		"Favorite list is empty"), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(favorites, HttpStatus.OK);
	}

	/**
	 * GET favorite by title for the authenticated user.
	 *
	 * @param principal the principal
	 * @param title The name of the favorite
	 * @return HttpStatus.OK if favorites are found or HttpStatus.NOT_FOUND in other case
	 */
	@GetMapping(value = "/{title}")
	public ResponseEntity<?> getFavorite( final Principal principal, @PathVariable final String title) {
		LOGGER.info("Fetching favorite for user '{}' with title '{}'", principal.getName(), title);
		this.validateUser(principal);

		final Optional<Favorite> favorite = this.favoriteRepository.findByAccountUserNameAndTitle(
				principal.getName(), title);

		if (!favorite.isPresent()) {
			LOGGER.error("Favorite for user '{}' with title '{}' not found.", principal.getName(), title);
			return new ResponseEntity<>(new CustomErrorType(
					"Favorite with title '" + title + "' not found"), HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(favorite.get(), HttpStatus.OK);
	}

	/**
	 * POST favorite. Create new favorite for the authenticated user
	 *
	 * @param principal the principal
	 * @param ucBuilder the uc builder
	 * @param newFavorite The favorite object to create
	 * @return HttpStatus.CREATED or HttpStatus.CONFLICT if already exists
	 */
	@PostMapping
	public ResponseEntity<?> createFavorite( final Principal principal,
											 final UriComponentsBuilder ucBuilder,
											 @RequestBody final Favorite newFavorite ) {
		LOGGER.info("Creating (POST) favorite with title '{}'", newFavorite.getTitle());
		this.validateUser(principal);

		if ( this.favoriteRepository.findByAccountUserNameAndTitle(
				principal.getName(),
				newFavorite.getTitle()).isPresent() ) {

			 LOGGER.error("Unable to create. A favorite with title '{}' already exist", newFavorite.getTitle());

			 return new ResponseEntity<>(new CustomErrorType(
	        		 "Unable to create. A Favorite with title '" + newFavorite.getTitle() + "' already exist."),
	        		 HttpStatus.CONFLICT); // 409
	    }


		return this.accountRepository.findByUserName(principal.getName())
				.map(account -> {
							final Favorite result = this.favoriteRepository.save(
									new Favorite(account,newFavorite.getTitle()));
							final HttpHeaders headers = new HttpHeaders();
							headers.setLocation(ucBuilder.path("/{id}").buildAndExpand(result.getId()).toUri());

							return new ResponseEntity<>(headers, HttpStatus.CREATED);
						}
				).orElse(ResponseEntity.noContent().build());
	}

	/**
	 * PUT (Update) the favorite by id.
	 *
	 * @param id The favorite ID in database
	 * @param newFavorite Object to replace
	 * @return HttpStatus.OK or HttpStatus.NOT_FOUND if not exists
	 */
	 @PutMapping(value = "/{id}")
	 public ResponseEntity<?> updateFavorite( @PathVariable final Long 		id,
			 								  @RequestBody  final Favorite  newFavorite) {

		 LOGGER.info("Updating (PUT) favorite with id '{}'", id);

		 final Optional<Favorite> favoriteForUpdate = this.favoriteRepository.findById(id);
		 if ( ! favoriteForUpdate.isPresent() ) {
			 LOGGER.error("Unable to update. Favorite with id '{}' not found.", id);
			 return new ResponseEntity<>(new CustomErrorType(
	            		"Unable to update. Favorite with id '" + id + "' not found."),
	                    HttpStatus.NOT_FOUND);
		 }

		 favoriteForUpdate.get().setTitle(newFavorite.getTitle());
		 this.favoriteRepository.save(favoriteForUpdate.get());
		 return new ResponseEntity<>(favoriteForUpdate, HttpStatus.OK);
	 }

	 /**
 	 * DELETE Favorite by id.
 	 *
 	 * @param id The favorite ID in database
 	 * @return HttpStatus.NO_CONTENT if favorite has been deleted. HttpStatus.NOT_FOUND in other case
 	 */
	 @DeleteMapping ( value = "/{id}")
	 public ResponseEntity<?> deleteFavorite( @PathVariable final  Long id) {
		 LOGGER.info("Fetching & Deleting favorite with id '{}'", id);

		 final Optional<Favorite> favoriteForDelete = this.favoriteRepository.findById(id);
		 if ( ! favoriteForDelete.isPresent() ) {
			 LOGGER.error("Unable to delete. Favorite with id '{}' not found.", id);
			 return new ResponseEntity<>(new CustomErrorType(
	            		"Unable to delete. Favorite with id '" + id + "' not found."),
	                    HttpStatus.NOT_FOUND);
		 }

		 this.favoriteRepository.deleteById(id);
		 return new ResponseEntity<>(HttpStatus.NO_CONTENT);
	 }

	 /**
 	 * Validate user.
 	 *
 	 * @param principal the principal
 	 */
 	// ---------------------------------- Private Methods ----------------------------------------
	 private void validateUser(final Principal principal) {
		 this.accountRepository
		 	.findByUserName(principal.getName())
		 	.orElseThrow(
				 () -> new UserNotFoundException(principal.getName()));
	 }
}

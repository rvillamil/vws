/**
 * Created by Rodrigo
 *
 * 10:27:16
 */
package es.rvp.web.vws.resources.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


/**
 * The Class UserNotFoundException.
 *
 * @author Rodrigo Villamil Pérez
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException extends RuntimeException {

	/** The Constant serialVersionUID. */
	private static final long serialVersionUID = 4912472634624855772L;

	/**
	 * Instantiates a new user not found exception.
	 *
	 * @param userId the user id
	 */
	public UserNotFoundException(final String userId) {
		super("WARNING! Could not find user '" + userId + "'");
	}
}
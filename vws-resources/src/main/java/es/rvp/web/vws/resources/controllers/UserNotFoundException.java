/**
 * Created by Rodrigo
 *
 * 10:27:16
 */
package es.rvp.web.vws.resources.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @author Rodrigo Villamil Pérez
 */
@ResponseStatus(HttpStatus.NOT_FOUND)
class UserNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 4912472634624855772L;

	public UserNotFoundException(final String userId) {
		super("WARNING! Could not find user '" + userId + "'");
	}
}
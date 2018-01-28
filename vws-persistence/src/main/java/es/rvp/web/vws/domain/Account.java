package es.rvp.web.vws.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import lombok.Getter;

// TODO: Auto-generated Javadoc
/**
 * The Class Account.
 *
 * @author Rodrigo Villamil Pérez
 */

/**
 * Gets the user name.
 *
 * @return the user name
 */
@Getter
@Entity // This tells Hibernate to make a table out of this class
public class Account {

	/** The favorites. */
	@OneToMany(mappedBy = "account")
	private final Set<Favorite> favorites = new HashSet<>();

	/** The id. */
	@Id
	@GeneratedValue
	private Long id;


	/** The password. */
	private String password;

	/** The user name. */
	@Column(unique=true)
	private String userName;

	/**
	 * Instantiates a new account.
	 *
	 * @param userName the user name
	 * @param password the password
	 */
	public Account(final String userName, final String password) {
		this.userName = userName;
		this.password = password;
	}

	/**
	 * Instantiates a new account.
	 */
	Account() { // jpa only
	}

	/**
	 * Gets the favorites.
	 *
	 * @return the favorites
	 */
	public Set<Favorite> getFavorites() {
		return this.favorites;
	}
}

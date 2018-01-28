package es.rvp.web.vws.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

// TODO: Auto-generated Javadoc
/**
 * The Class Favorite.
 *
 * @author Rodrigo Villamil Pérez
 */

/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@EqualsAndHashCode(of = { "title" })

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@ToString
@Entity // This tells Hibernate to make a table out of this class
public class Favorite {

	/** The id. */
	@Id
	@GeneratedValue
	
	/**
	 * Gets the id.
	 *
	 * @return the id
	 */
	@Getter
	private Long id;

	/** The title. */
	@NonNull
	
	/**
	 * Gets the title.
	 *
	 * @return the title
	 */
	@Getter
	
	/**
	 * Sets the title.
	 *
	 * @param title the new title
	 */
	@Setter
	private String title;

	/** The account. */
	@JsonIgnore
	@ManyToOne
	private Account account;

	/**
	 * Instantiates a new favorite.
	 *
	 * @param account the account
	 * @param title the title
	 */
	public Favorite(final Account account, final String title) {
		this.account = account;
		this.title = title;
	}

	/**
	 * Instantiates a new favorite.
	 */
	Favorite() { // JPA only
	}
}

package es.rvp.web.vws.domain;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;

/**
 * @author Rodrigo Villamil Pérez
 */
@Getter
@Entity // This tells Hibernate to make a table out of this class
public class Account {

	@OneToMany(mappedBy = "account")
	private final Set<Favorite> favorites = new HashSet<>();

	@Id
	@GeneratedValue
	private Long id;

	@JsonIgnore
	private String password;

	private String userName;

	public Account(final String userName, final String password) {
		this.userName = userName;
		this.password = password;
	}

	Account() { // jpa only
	}

	public Set<Favorite> getFavorites() {
		return this.favorites;
	}
}

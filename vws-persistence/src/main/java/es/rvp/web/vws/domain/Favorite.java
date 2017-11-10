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

/**
 * @author Rodrigo Villamil Pérez
 */
@EqualsAndHashCode(of = { "title" })
@ToString
@Entity // This tells Hibernate to make a table out of this class
public class Favorite {

	@Id
	@GeneratedValue
	@Getter
	private Long id;

	@NonNull
	@Getter
	@Setter
	private String title;

	@JsonIgnore
	@ManyToOne
	private Account account;

	public Favorite(final Account account, final String title) {
		this.account = account;
		this.title = title;
	}

	Favorite() { // JPA only
	}
}

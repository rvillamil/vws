package es.rvp.web.vws.domain;

import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.ToString;

/**
 * @author Rodrigo Villamil Pérez
 */
@Getter
@Setter
@EqualsAndHashCode(of = {"title"})
@ToString
@Entity
public class Favorite {
	@Id
	@NonNull
	private String title;
}


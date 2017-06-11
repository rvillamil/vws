package es.rvp.web.vws.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
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
	@GeneratedValue(strategy = GenerationType.AUTO)
	private long id;
	@NonNull
	private String title;
}


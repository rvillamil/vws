package es.rvp.web.vws.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Builder;

/**
 * The Show object. Requieres lombok project
 *
 * @author Rodrigo Villamil Perez
 */
@Getter
@EqualsAndHashCode(of = {"title", "session", "episode"})
@ToString
@Builder
public class Show {

	@NonNull
	private final String title;
	private String baseURI;
	private final String session;
	private final String episode;
	//
	// TS-Screener,..etc
	//
	private final String quality;
	// 800Mb..etc
	private final String fileSize;
	private final Double filmaffinityPoints;
	// Release Date: 06/05/2016
	private final String releaseDate;
	private final String URLTODownlad;
	private final String URLWithCover;
	private final String description;
	private final String sinopsis;
}

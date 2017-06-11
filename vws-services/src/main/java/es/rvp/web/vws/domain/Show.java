package es.rvp.web.vws.domain;

import javax.persistence.Entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Builder;

/**
 * The Show object. Requieres lombok project
 * @author Rodrigo Villamil Perez
 * [[SuppressWarningsSpartan]]
 */
@Getter
@EqualsAndHashCode(of = {"title", "session", "episode"})
@ToString
@Builder
public class Show {

	@NonNull
	private final String title;
	private final String baseURI;
	private final String session;
	private final String episode;
	//
	// TS-Screener,..etc
	//
	private final String quality;
	// 800Mb..etc
	private final String fileSize;
	private final Double filmaffinityPoints;
	// Release Date: 06/05/2016d
	private final String releaseDate;
	private final String URLTODownload;
	private final String URLWithCover;
	private final String description;
	private final String sinopsis;
}

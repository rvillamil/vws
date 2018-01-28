package es.rvp.web.vws.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.experimental.Builder;

// TODO: Auto-generated Javadoc
/**
 * The Show object. Requieres lombok project
 * @author Rodrigo Villamil Perez
 * [[SuppressWarningsSpartan]]
 */

/**
 * Gets the sinopsis.
 *
 * @return the sinopsis
 */
@Getter

/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
@EqualsAndHashCode(of = {"title", "session", "episode"})

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@ToString

/* (non-Javadoc)
 * @see java.lang.Object#toString()
 */
@Builder
public class Show {

	/** The title. */
	@NonNull
	private final String title;
	
	/** The base URI. */
	private final String baseURI;
	
	/** The session. */
	private final String session;
	
	/** The episode. */
	private final String episode;
	//
	// TS-Screener,..etc
	/** The quality. */
	//
	private final String quality;
	
	/** The file size. */
	// 800Mb..etc
	private final String fileSize;
	
	/** The filmaffinity points. */
	private final Double filmaffinityPoints;
	
	/** The release date. */
	// Release Date: 06/05/2016d
	private final String releaseDate;
	
	/** The URLTO download. */
	private final String URLTODownload;
	
	/** The URL with cover. */
	private final String URLWithCover;
	
	/** The description. */
	private final String description;
	
	/** The sinopsis. */
	private final String sinopsis;
}

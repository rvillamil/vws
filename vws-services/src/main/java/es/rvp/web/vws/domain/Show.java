package es.rvp.web.vws.domain;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

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
	public String title="";
	
	/** The base URI. */
	public String baseURI="";
	
	/** The session. */
	public String session="";
	
	/** The episode. */
	public String episode="";
	//
	// TS-Screener,..etc
	/** The quality. */
	//
	public String quality="";
	
	/** The file size. */
	// 800Mb..etc
	public String fileSize="";
	
	/** The filmaffinity points. */
	public Double filmaffinityPoints=0.0;
	
	/** The release date. */
	// Release Date: 06/05/2016d
	public String releaseDate="";
	
	/** The URLTO download. */
	public String URLTODownload="";
	
	/** The URL with cover. */
	public String URLWithCover="";
	
	/** The description. */
	public String description="";
	
	/** The sinopsis. */
	public String sinopsis="";
}

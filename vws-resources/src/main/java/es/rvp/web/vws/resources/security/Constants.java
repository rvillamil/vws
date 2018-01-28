package es.rvp.web.vws.resources.security;

// TODO: Auto-generated Javadoc
/**
 * The Class Constants.
 */
public class Constants {

	// Spring Security

	/** The Constant LOGIN_URL. */
	public static final String LOGIN_URL = "/login";
	
	/** The Constant HEADER_AUTHORIZACION_KEY. */
	public static final String HEADER_AUTHORIZACION_KEY = "Authorization";
	
	/** The Constant TOKEN_BEARER_PREFIX. */
	public static final String TOKEN_BEARER_PREFIX = "Bearer ";

	// JWT

	/** The Constant ISSUER_INFO. */
	public static final String ISSUER_INFO = "https://github.com/rvillamil";
	
	/** The Constant SUPER_SECRET_KEY. */
	public static final String SUPER_SECRET_KEY = "1234";
	
	/** The Constant TOKEN_EXPIRATION_TIME. */
	public static final long TOKEN_EXPIRATION_TIME = 864_000_000; // 10 day

}

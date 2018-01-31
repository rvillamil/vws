package es.rvp.web.vws.resources.security;

import static es.rvp.web.vws.resources.security.Constants.HEADER_AUTHORIZACION_KEY;
import static es.rvp.web.vws.resources.security.Constants.SUPER_SECRET_KEY;
import static es.rvp.web.vws.resources.security.Constants.TOKEN_BEARER_PREFIX;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import io.jsonwebtoken.Jwts;

/**
 * La clase responsable de la autorización verifica la cabecera en busca de un token, se
 * verifica el token y se extrae la información del mismo para establecer la identidad del
 * usuario dentro del contexto de seguridad de la aplicación.
 *
 * No se requieren accesos adicionales a BD ya que al estar firmado digitalmente
 * si hay alguna alteración en el token se corrompe.
 *
 * @author Rodrigo Villamil Pérez
 *
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	/**
	 * Instantiates a new JWT authorization filter.
	 *
	 * @param authManager the auth manager
	 */
	public JWTAuthorizationFilter(final AuthenticationManager authManager) {
		super(authManager);
	}

	/* (non-Javadoc)
	 * @see org.springframework.security.web.authentication.www.BasicAuthenticationFilter#doFilterInternal(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(final HttpServletRequest req, final HttpServletResponse res, final FilterChain chain)
			throws IOException, ServletException {
		final String header = req.getHeader(HEADER_AUTHORIZACION_KEY);
		if (header == null || !header.startsWith(TOKEN_BEARER_PREFIX)) {
			chain.doFilter(req, res);
			return;
		}
		final UsernamePasswordAuthenticationToken authentication = this.getAuthentication(req);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		chain.doFilter(req, res);
	}

	/**
	 * Gets the authentication.
	 *
	 * @param request the request
	 * @return the authentication
	 */
	private UsernamePasswordAuthenticationToken getAuthentication(final HttpServletRequest request) {
		final String token = request.getHeader(HEADER_AUTHORIZACION_KEY);
		if (token != null) {
			// Se procesa el token y se recupera el usuario.
			final String user = Jwts.parser()
					.setSigningKey(SUPER_SECRET_KEY)
					.parseClaimsJws(token.replace(TOKEN_BEARER_PREFIX, ""))
					.getBody()
					.getSubject();

			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}
			return null;
		}
		return null;
	}
}

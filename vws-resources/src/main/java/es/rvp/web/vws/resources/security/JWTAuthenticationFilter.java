package es.rvp.web.vws.resources.security;

import static es.rvp.web.vws.resources.security.Constants.HEADER_AUTHORIZACION_KEY;
import static es.rvp.web.vws.resources.security.Constants.ISSUER_INFO;
import static es.rvp.web.vws.resources.security.Constants.SUPER_SECRET_KEY;
import static es.rvp.web.vws.resources.security.Constants.TOKEN_BEARER_PREFIX;
import static es.rvp.web.vws.resources.security.Constants.TOKEN_EXPIRATION_TIME;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import es.rvp.web.vws.domain.Account;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Haciendo uso de las clases proporcionadas por Spring Security, extendemos
 * su comportamiento para reflejar nuestras necesidades.
 *
 * Se verifica que las credencias proporcionadas son válidas y se genera el JWT.
 *
 * @author Rodrigo Villamil Pérez
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

	private final AuthenticationManager authenticationManager;

	public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}

	@Override
	public Authentication attemptAuthentication(
			HttpServletRequest request,
			HttpServletResponse response )

					throws AuthenticationException {
		try {
			final Account account = new ObjectMapper().readValue(
					request.getInputStream(), Account.class);

			return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
					account.getUserName(), account.getPassword(), new ArrayList<>()));

		} catch (final IOException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * No hay obligación de devolver el token en la cabecera ni con una clave concreta
	 * pero se recomienda seguir los estándares utilizados en la actualidad (RFC 2616, RFC 6750).
	 *
	 * Lo habitual es devolverlo en la cabecera HTTP utilizando la clave “Authorization” e
	 * indicando que el valor es un token “Bearer “ + token
	 *
	 * Este token lo deberá conservar vuestro cliente web en su localstorage y remitirlo
	 * en las peticiones posteriores que se hagan al API.
	 */
	@Override
	protected void successfulAuthentication(
			HttpServletRequest    request,
			HttpServletResponse   response,
			FilterChain 		  chain,
			Authentication 	  	  auth )

					throws IOException, ServletException {

		final String token = Jwts.builder().setIssuedAt(new Date()).setIssuer(ISSUER_INFO)
				.setSubject(((User)auth.getPrincipal()).getUsername())
				.setExpiration(new Date(System.currentTimeMillis() + TOKEN_EXPIRATION_TIME))
				.signWith(SignatureAlgorithm.HS256, SUPER_SECRET_KEY).compact();
		response.addHeader(HEADER_AUTHORIZACION_KEY, TOKEN_BEARER_PREFIX + " " + token);
	}
}

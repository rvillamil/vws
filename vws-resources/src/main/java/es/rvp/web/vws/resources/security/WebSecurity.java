package es.rvp.web.vws.resources.security;

import static es.rvp.web.vws.resources.security.Constants.LOGIN_URL;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

	private final UserDetailsService userDetailsService;

	public WebSecurity(UserDetailsService userDetailsService) {
		this.userDetailsService = userDetailsService;
	}

	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}


	/**
 	 * Podemos observar que se ajusta la configuración para CORS y
	 * se desactiva el filtro de Cross-site request forgery (CSRF).
	 * Esto nos permite habilitar el API para cualquier dominio,
	 * esta es una de las grandes ventajas del uso de JWT.
	 *
     * FIXME 00: Revisando el asunto del CORS
	 */
	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		/*
		 * 1. Se desactiva el uso de cookies
		 * 2. Se activa la configuración CORS con los valores por defecto
		 * 3. Se desactiva el filtro CSRF
		 * 4. Se indica que el login, las paginas de swagger y el H2 en memoria NO requiere autenticación
		 * 5. Se indica que el resto de URLs esten securizadas
		 */
		httpSecurity
		.sessionManagement().sessionCreationPolicy(
				SessionCreationPolicy.STATELESS).and().cors().and().csrf().disable()

		.authorizeRequests()
		.antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()
		.antMatchers("/health").permitAll() // spring-boot-actuator
		.antMatchers("/v2/api-docs", // swagger
					 "/configuration/ui",
					 "/swagger-resources/**",
					 "/configuration/**",
					 "/swagger-ui.html",
					 "/webjars/**").permitAll()
		.antMatchers("/h2/**").permitAll() // h2-embedded
		.anyRequest().authenticated().and().addFilter(
				new JWTAuthenticationFilter(authenticationManager()))
		.addFilter(
				new JWTAuthorizationFilter(authenticationManager()));

	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		// Se define la clase que recupera los usuarios y el algoritmo para procesar las passwords
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
}
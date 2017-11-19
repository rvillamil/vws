package es.rvp.web.vws.resources.security;

import static es.rvp.web.vws.resources.security.Constants.LOGIN_URL;

import java.util.Arrays;

import org.h2.server.web.WebServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
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

/**
 * Example: https://auth0.com/blog/implementing-jwt-authentication-on-spring-boot/
 *
 * @author Rodrigo Villamil Pérez
 */
@Configuration
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private static final Logger LOGGER 			= LoggerFactory.getLogger(WebSecurity.class);

    private final UserDetailsService userDetailsService;

    public WebSecurity(final UserDetailsService userDetailsService) {
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
     */
    @Override
    protected void configure(final HttpSecurity httpSecurity) throws Exception {
        /*
         * 1. Se desactiva el uso de cookies
         * 2. Se activa la configuración CORS con los valores por defecto
         * 3. Se desactiva el filtro CSRF
         * 4. Se autoriza el acceso al path /login, al contenido 'swagger' y al h2
         * 5. Se indica que el resto de URLs esten securizadas
         */
        httpSecurity
        .sessionManagement().sessionCreationPolicy(
                SessionCreationPolicy.STATELESS)
        .and().cors() // by default uses a Bean by the name of 'corsConfigurationSource'
        .and().csrf().disable()

        .authorizeRequests()
            .antMatchers(HttpMethod.POST, LOGIN_URL).permitAll()

            .antMatchers("/health").permitAll() // spring-boot-actuator
            .antMatchers("/info").permitAll()
            .antMatchers("/springbeans").permitAll()
            .antMatchers("/loggers").permitAll()
            .antMatchers("/metrics").permitAll()
            .antMatchers("/mappings").permitAll()
            .antMatchers("/docs/**").permitAll()

            .antMatchers("/v2/api-docs", // swagger
                     "/configuration/ui",
                     "/swagger-resources/**",
                     "/configuration/**",
                     "/swagger-ui.html",
                     "/webjars/**").permitAll()
            .antMatchers("/h2/**").permitAll() // h2-embedded

            .anyRequest().authenticated()
            .and()
            .addFilter(
                new JWTAuthenticationFilter(authenticationManager()))
            .addFilter(
                new JWTAuthorizationFilter(authenticationManager()));
    }

    @Override
    public void configure(final AuthenticationManagerBuilder auth) throws Exception {
        // Se define la clase que recupera los usuarios y el algoritmo para procesar las passwords
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
    }

    /**
     * CORS configuration: https://docs.spring.io/spring-security/site/docs/current/reference/html/cors.html
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setExposedHeaders(Arrays.asList("Authorization")); // https://stackoverflow.com/questions/1557602/jquery-and-ajax-response-header
        configuration.applyPermitDefaultValues();

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    @Profile("default")
    ServletRegistrationBean h2servletRegistration() throws Exception{
        LOGGER.info ("Loading H2 configuration for default profile (not production)!");
        // see: https://dzone.com/articles/using-the-h2-database-console-in-spring-boot-with
        this.getHttp().headers().frameOptions().disable();

        ServletRegistrationBean registrationBean = new ServletRegistrationBean( new WebServlet());
        registrationBean.addUrlMappings("/h2/*");

        return registrationBean;
    }
}

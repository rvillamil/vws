package es.rvp.web.vws;


import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;

/**
 * For run in servlet container .e.g.: Tomcat
 * This makes use of Spring Framework’s Servlet 3.0 support and allows you to configure your application
 * when it’s launched by the servlet container.
 *
 * @see http://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto-create-a-deployable-war-file
 * @author Rodrigo Villamil Pérez
 */
public class ServletInitializer extends SpringBootServletInitializer {

	@Override
	protected SpringApplicationBuilder configure(final SpringApplicationBuilder application) {
		return application.sources(ApplicationVWS.class);
	}
}

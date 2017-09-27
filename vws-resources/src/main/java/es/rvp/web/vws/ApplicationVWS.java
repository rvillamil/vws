package es.rvp.web.vws;

import java.util.Arrays;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import es.rvp.web.vws.components.jsoup.JSoupHelper;
import es.rvp.web.vws.components.jsoup.JSoupHelperImpl;
import es.rvp.web.vws.domain.Account;
import es.rvp.web.vws.domain.AccountRepository;
import es.rvp.web.vws.domain.FavoriteRepository;
import es.rvp.web.vws.domain.ShowFactory;
import es.rvp.web.vws.domain.ShowFieldParser;
import es.rvp.web.vws.domain.tumejortorrent.ShowEpisodeParser;
import es.rvp.web.vws.domain.tumejortorrent.ShowFactoryImpl;
import es.rvp.web.vws.domain.tumejortorrent.ShowQualityParser;
import es.rvp.web.vws.domain.tumejortorrent.ShowSessionParser;
import es.rvp.web.vws.domain.tumejortorrent.ShowURLToDownloadParser;

/**
 * Spring boot Application: https://spring.io/guides/gs/spring-boot/
 *
 * @author Rodrigo Villamil Perez
 */
@SpringBootApplication
public class ApplicationVWS {
	/**
	 * Main method for spring boot applications
	 * @param args argument list
	 */
	public static void main(final String[] args) {
		SpringApplication.run(ApplicationVWS.class, args);
	}


	@Bean
	CommandLineRunner init( final AccountRepository accountRepository,
						    final FavoriteRepository favoriteRepository ) {
		return (evt) -> Arrays.asList(
				"rodrigo,olga".split(","))
				.forEach(
						a -> {
							final Account account = accountRepository.save(new Account(a,
									"password"));
							//favoriteRepository.save(new Bookmark(account,
							//		"http://bookmark.com/1/" + a, "A description"));
							//favoriteRepository.save(new Bookmark(account,
							//		"http://bookmark.com/2/" + a, "A description"));
						});
	}

	// CORS
	// FIXME 00: Revisando el asunto del CORS (ver en https://spring.io/guides/tutorials/bookmarks/#_securing_a_rest_service)

	/*
	@Bean
	FilterRegistrationBean corsFilter(@Value("${tagit.origin:http://localhost:9090}") String origin) {
		return new FilterRegistrationBean(new Filter() {

			public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			        throws IOException, ServletException {
				HttpServletRequest request = (HttpServletRequest)req;
				HttpServletResponse response = (HttpServletResponse)res;
				String method = request.getMethod();
				// this origin value could just as easily have come from a database
				response.setHeader("Access-Control-Allow-Origin", origin);
				response.setHeader("Access-Control-Allow-Methods", "POST,GET,OPTIONS,DELETE");
				response.setHeader("Access-Control-Max-Age", Long.toString(60 * 60));
				response.setHeader("Access-Control-Allow-Credentials", "true");
				response.setHeader("Access-Control-Allow-Headers",
				        "Origin,Accept,X-Requested-With,Content-Type,Access-Control-Request-Method,Access-Control-Request-Headers,Authorization");
				if ("OPTIONS".equals(method)) {
					response.setStatus(HttpStatus.OK.value());
				} else {
					chain.doFilter(req, res);
				}
			}

			public void init(FilterConfig filterConfig) {
			}

			public void destroy() {
			}
		});
	}
	*/

	// ---------------------- Application config -----------------------------
	@Bean
	JSoupHelper jSoupHelper() {
		return new JSoupHelperImpl();
	}

	@Bean
	ShowFieldParser showEpisodeParser() {
		return new ShowEpisodeParser(this.jSoupHelper());
	}

	@Bean
	ShowFieldParser showQualityParser() {
		return new ShowQualityParser(this.jSoupHelper());
	}

	@Bean
	ShowFieldParser showSessionParser() {
		return new ShowSessionParser(this.jSoupHelper());
	}

	@Bean
	ShowFieldParser showURLToDownloadParser() {
		return new ShowURLToDownloadParser();
	}

	@Bean
	ShowFactory showFactory() {
		return new ShowFactoryImpl(
				this.jSoupHelper(),
				this.showEpisodeParser(),
				this.showQualityParser(),
				this.showSessionParser(),
				this.showURLToDownloadParser());
	}
}
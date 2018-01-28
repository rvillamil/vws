package es.rvp.web.vws;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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

// TODO: Auto-generated Javadoc
/**
 * Spring boot Application: https://spring.io/guides/gs/spring-boot/
 *
 * @author Rodrigo Villamil Perez
 */
@SpringBootApplication
public class ApplicationVWS {

	/** The Constant LOGGER. */
	// LOGGER
	private static final Logger LOGGER = LoggerFactory.getLogger(ApplicationVWS.class);

	/**
	 * Main method for spring boot applications.
	 *
	 * @param args argument list
	 */
	public static void main(final String[] args) {
		SpringApplication.run(ApplicationVWS.class, args);
	}


	/**
	 * Inits the.
	 *
	 * @param accountRepository the account repository
	 * @param favoriteRepository the favorite repository
	 * @param bCryptPasswordEncoder the b crypt password encoder
	 * @return the command line runner
	 */
	/*
	 * For testing purposes:
	 *
	 * curl -i -H "Content-Type: application/json" -X POST -d '{ "userName": "admin", "password": "password"}' http://localhost:8080/login
     *
	 */
	@Bean
	@Profile("default")
	CommandLineRunner init( final AccountRepository accountRepository,
						    final FavoriteRepository favoriteRepository,
						    final BCryptPasswordEncoder bCryptPasswordEncoder) {
		LOGGER.info ("Loading users/password for testing purposes: rodrigo/pepe and olga/lola");
		return args -> {
			accountRepository.save(new Account("rodrigo", bCryptPasswordEncoder.encode ("pepe")));
			accountRepository.save(new Account("olga", bCryptPasswordEncoder.encode ("lola")));
		};
	}

	/**
	 * B crypt password encoder.
	 *
	 * @return the b crypt password encoder
	 */
	// ---------------------- Application config -----------------------------
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	/**
	 * J soup helper.
	 *
	 * @return the j soup helper
	 */
	@Bean
	JSoupHelper jSoupHelper() {
		return new JSoupHelperImpl();
	}

	/**
	 * Show episode parser.
	 *
	 * @return the show field parser
	 */
	@Bean
	ShowFieldParser showEpisodeParser() {
		return new ShowEpisodeParser(this.jSoupHelper());
	}

	/**
	 * Show quality parser.
	 *
	 * @return the show field parser
	 */
	@Bean
	ShowFieldParser showQualityParser() {
		return new ShowQualityParser(this.jSoupHelper());
	}

	/**
	 * Show session parser.
	 *
	 * @return the show field parser
	 */
	@Bean
	ShowFieldParser showSessionParser() {
		return new ShowSessionParser(this.jSoupHelper());
	}

	/**
	 * Show URL to download parser.
	 *
	 * @return the show field parser
	 */
	@Bean
	ShowFieldParser showURLToDownloadParser() {
		return new ShowURLToDownloadParser();
	}

	/**
	 * Show factory.
	 *
	 * @return the show factory
	 */
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
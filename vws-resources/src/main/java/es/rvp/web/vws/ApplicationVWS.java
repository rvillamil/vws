package es.rvp.web.vws;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
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


	/*
	 * For testing purposes:
	 *
	 * curl -i -H "Content-Type: application/json" -X POST -d '{ "userName": "admin", "password": "password"}' http://localhost:8080/login
     *
	 * username: "admin"
	 * password: "password" <--> $2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP.
	 *
	 */
	@Bean
	CommandLineRunner init( final AccountRepository accountRepository,
						    final FavoriteRepository favoriteRepository,
						    final BCryptPasswordEncoder bCryptPasswordEncoder) {

		return args -> {
			accountRepository.save(new Account("rodrigo", bCryptPasswordEncoder.encode ("pepe")));
			accountRepository.save(new Account("olga", bCryptPasswordEncoder.encode ("lola")));
		};
	}

	// ---------------------- Application config -----------------------------
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

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
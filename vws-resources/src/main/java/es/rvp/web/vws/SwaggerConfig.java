package es.rvp.web.vws;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	@Bean
	public Docket productApi() {
		return new Docket(DocumentationType.SWAGGER_2)
				.select().apis(RequestHandlerSelectors.basePackage("es.rvp.web.vws.resources.controllers"))
				.paths(PathSelectors.any())
				.build().apiInfo(this.metaData());

	}
	// Visit: http://localhost:8080/swagger-ui.html
	private ApiInfo metaData() {
		return new ApiInfo(
				"VWS REST API",
				"REST API for Video websites scraper",
				"1.0",
				"Terms of service",
				new Contact("Rodrigo Villamil",
						"https://github.com/rvillamil/", "rodrigo.villamil@gmail.com"),
				"GNU General Public License v3.0",
				"https://www.gnu.org/licenses/gpl-3.0.en.html");
	}
}
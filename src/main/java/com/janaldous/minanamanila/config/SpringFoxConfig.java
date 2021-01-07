package com.janaldous.minanamanila.config;

import java.util.ArrayList;
import java.util.Optional;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import springfox.bean.validators.configuration.BeanValidatorPluginsConfiguration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
@Import(BeanValidatorPluginsConfiguration.class)
public class SpringFoxConfig {

	private static final Contact DEFAULT_CONTACT = new Contact("Jat Torres", "http://www.minanamanila.com/",
			"jat.torres@gmail.com");

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(new ApiInfo("minanamanila-api", "Minanamanila API", "1.0", "urn:tos", DEFAULT_CONTACT, "Apache 2.0", "http://www.apache.org/licenses/LICENSE-2.0", new ArrayList<>()))
				.select()
				.apis(RequestHandlerSelectors.basePackage("com.janaldous.minanamanila.webfacade"))
				.paths(PathSelectors.any()).build().genericModelSubstitutes(Optional.class);
	}
}
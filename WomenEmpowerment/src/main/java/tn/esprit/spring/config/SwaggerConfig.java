package tn.esprit.spring.config;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.ApiKey;
import springfox.documentation.service.AuthorizationScope;
import springfox.documentation.service.Contact;
import springfox.documentation.service.SecurityReference;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spi.service.contexts.SecurityContext;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
	
	private ApiKey apiKey() { 
	    return new ApiKey("JWT", "Authorization", "header"); 
	}
	
	private SecurityContext securityContext() { 
	    return SecurityContext.builder().securityReferences(defaultAuth()).build(); 
	} 

	private List<SecurityReference> defaultAuth() { 
	    AuthorizationScope authorizationScope = new AuthorizationScope("global", "accessEverything"); 
	    AuthorizationScope[] authorizationScopes = new AuthorizationScope[1]; 
	    authorizationScopes[0] = authorizationScope; 
	    return Arrays.asList(new SecurityReference("JWT", authorizationScopes)); 
	}

	@Bean
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				.apiInfo(apiInfo())
			      .securityContexts(Arrays.asList(securityContext()))
			      .securitySchemes(Arrays.asList(apiKey()))
			      .select()
			      .apis(RequestHandlerSelectors.basePackage("tn.esprit.spring.controllers"))
			      .paths(PathSelectors.any())
			      .build();
	}
	
	private ApiInfo apiInfo() {
	    return new ApiInfo(
	      "Women Empowerment REST API",
	      "This API serves to test end points of the application",
	      "1.0",
	      "Terms of service",
	      new Contact("Les Elites", "www.esprit.tn", "seifeddine.denguezli@esprit.com"),
	      "License of API",
	      "API license URL",
	      Collections.emptyList());
	}
}
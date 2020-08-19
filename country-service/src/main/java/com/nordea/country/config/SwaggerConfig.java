package com.nordea.country.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;

@Configuration
public class SwaggerConfig {
    private ApiInfo getApiInfo() {
        return new ApiInfoBuilder().title("Nordea Country Service").version("1.0")
                .description("API for country service").build();
    }

    @Bean
    public Docket countryServiceApi() {
        return new Docket(DocumentationType.SWAGGER_2).apiInfo(this.getApiInfo()).enable(true)
                .select().apis(RequestHandlerSelectors.any()).paths(PathSelectors.any()).build();
    }
}

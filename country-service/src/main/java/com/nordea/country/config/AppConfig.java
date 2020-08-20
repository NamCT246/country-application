package com.nordea.country.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {
    @Bean
    WebClient loadWebClient() {
        return WebClient.create("http://localhost:8080");
    }

    @Bean
    public String apiHost() {
        return "https://restcountries.eu/rest/v2";
    }
}

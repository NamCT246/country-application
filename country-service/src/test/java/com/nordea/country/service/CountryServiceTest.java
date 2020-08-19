package com.nordea.country.service;

import com.nordea.country.dto.CountriesListResponseDto;
import com.nordea.country.dto.CountryResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.CoreMatchers.*;
// import com.jayway.jsonpath.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CountryServiceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    @DisplayName("Should get a list of all countries and return as a JSON with  the key countries")
    void getAllCountries() {
        webTestClient.get().uri("/api/countries/").exchange().expectStatus().isOk()
                .expectBody(CountriesListResponseDto.class);
    }

    @Test
    @DisplayName("Should get a JSON contains data about the country")
    void getCountryByName() {
        webTestClient.get().uri("api/countries/{name}", "finland").exchange().expectStatus().isOk()
                .expectBody(CountryResponseDto.class);
    }
}

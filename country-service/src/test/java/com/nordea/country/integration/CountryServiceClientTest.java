package com.nordea.country.integration;

import com.nordea.country.dto.CountriesRequestDto;
import com.nordea.country.dto.CountryRequestDto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CountryServiceClientTest {
    @Autowired
    private WebTestClient webTestClient;

    @Value("${country.rest.api.endpoint}")
    private String countryEndpoint;

    @Test
    @DisplayName("Should get a list of countries that contain country data")
    public void getAllCountriesFromService() {

        /**
         * Could not.expectHeader().contentType(MediaType.APPLICATION_JSON) as the external service
         * media type also has ut8, which is complied with most modern browser Spring version > 5
         * not support this?
         */
        webTestClient.get().uri(countryEndpoint + "/all").exchange().expectStatus().isOk()
                .expectBodyList(CountriesRequestDto.class);

    }

    @Test
    @DisplayName("Should get a list of 1 country that has the same name")
    public void getCountryByNameFromService() {
        webTestClient.get().uri(countryEndpoint + "/name/{name}", "finland").exchange()
                .expectStatus().isOk().expectBodyList(CountryRequestDto.class);
    }

}

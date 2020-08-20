package com.nordea.country.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.nordea.country.CountryApplication;
import com.nordea.country.config.AppConfig;
import com.nordea.country.config.AppConfigTest;
import com.nordea.country.dto.CountriesRequestDto;
import com.nordea.country.dto.CountriesResponseDto;
import com.nordea.country.dto.CountryRequestDto;
import com.nordea.country.dto.CountryResponseDto;
import com.nordea.country.exceptions.CountryServiceException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import lombok.extern.slf4j.Slf4j;

import com.nordea.country.CountryApplicationTests;
import static com.github.tomakehurst.wiremock.client.WireMock.*;


@ActiveProfiles({"test"})
@SpringBootTest(classes = {CountryApplication.class, AppConfigTest.class})
@PropertySources({@PropertySource(value = "classpath:application-test.properties")})
public class WiremockWebClientTest {

        private WireMockServer mockServer;

        @Autowired
        private ObjectMapper objectMapper;

        @Autowired
        private CountryServiceClient countryServiceClient;

        @BeforeEach
        private void beforeEach() {
                mockServer = new WireMockServer(WireMockConfiguration.wireMockConfig()
                                .port(AppConfigTest.API_PORT));
                mockServer.start();
        }

        @AfterEach
        private void afterEach() {
                mockServer.stop();
        }

        @Test
        void testGetAllCountries() throws JsonProcessingException {
                CountriesRequestDto afghanistan = new CountriesRequestDto("Afghanistan", "AF");
                CountriesRequestDto aland = new CountriesRequestDto("Åland Islands", "AX");
                List<CountriesRequestDto> dummyCountryList = new ArrayList<CountriesRequestDto>();

                dummyCountryList.add(afghanistan);
                dummyCountryList.add(aland);

                String jsonBody = objectMapper.writeValueAsString(dummyCountryList);

                mockServer.stubFor(get(urlEqualTo("/all/")).willReturn(
                                aResponse().withHeader("Content-Type", "application/json")
                                                .withStatus(200).withBody(jsonBody)));

                Flux<CountriesRequestDto> countriesListRequest =
                                countryServiceClient.getAllCountriesFromService();

                StepVerifier.create(countriesListRequest).expectNext(afghanistan).expectNext(aland)
                                .expectComplete().verify();
        }

        @Test
        void testGetCountryByName() throws JsonProcessingException {
                String countryName = "finland";

                CountryRequestDto finland = CountryRequestDto.builder().alpha2Code("FI")
                                .capital("Helsinki").flag("something").name("Finland")
                                .population((long) 5491817).build();

                String jsonBody = objectMapper.writeValueAsString(finland);

                mockServer.stubFor(get("/name/" + countryName)
                                .willReturn(aResponse().withStatus(200).withBody(jsonBody)));

                Mono<CountryRequestDto> countryRequest =
                                countryServiceClient.getCountryByNameFromService(countryName);

                StepVerifier.create(countryRequest).expectNext(finland).expectComplete();
        }

        @Test
        void testNonExistCountryName() {
                String nonExistName = "vietfin";

                mockServer.stubFor(get("/name/" + nonExistName)
                                .willReturn(aResponse().withStatus(404)));

                Mono<CountryRequestDto> countryRequest =
                                countryServiceClient.getCountryByNameFromService(nonExistName);

                StepVerifier.create(countryRequest).expectError(CountryServiceException.class)
                                .verify();
        }
}

package com.nordea.country.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
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
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class WiremockWebClientTest {
    @Value("${country.rest.api.endpoint}")
    private String countryEndpoint;

    private WireMockServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CountryServiceClient countryServiceClient;

    @BeforeEach
    private void beforeEach() {
        mockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        mockServer.start();
    }

    @AfterEach
    private void afterEach() {
        mockServer.stop();
    }

    @Test
    void testGetAllCountries() throws JsonProcessingException {
        CountriesResponseDto afghanistan = new CountriesResponseDto("Afghanistan", "AF");
        CountriesResponseDto aland = new CountriesResponseDto("Åland Islands", "AX");
        List<CountriesResponseDto> dummyCountryList = new ArrayList<CountriesResponseDto>();

        dummyCountryList.add(afghanistan);
        dummyCountryList.add(aland);

        String jsonBody = objectMapper.writeValueAsString(Arrays.asList(dummyCountryList));

        mockServer.stubFor(get(urlEqualTo(countryEndpoint + "/all"))
                .willReturn(aResponse().withStatus(200).withBody(jsonBody)));
    }

    @Test
    void testGetCountryByName() throws JsonProcessingException {
        CountryResponseDto finland = CountryResponseDto.builder().name("Finland")
                .capital("Helsinki").countryCode("FI").flagFileUrl("some string").build();

        String jsonBody = objectMapper.writeValueAsString(finland);

        mockServer.stubFor(get(countryEndpoint + "/name" + "finland")
                .willReturn(aResponse().withStatus(200).withBody(jsonBody)));
    }

    @Test
    void testNonExistCountryName() {
        String nonExistName = "vietfin";

        mockServer.stubFor(get(countryEndpoint + "/name/" + nonExistName)
                .willReturn(aResponse().withStatus(404)));
    }

    @Test
    void testRequestGetCountries() {
        Flux<CountriesRequestDto> countriesListRequest =
                countryServiceClient.getAllCountriesFromService();

        StepVerifier.create(countriesListRequest)
                .expectNext(new CountriesRequestDto("Afghanistan", "AF"))
                .expectNext(new CountriesRequestDto("Åland Islands", "AX")).expectComplete();
    }

    @Test
    void testRequestGetCountryByName() {
        String countryName = "Finland";
        Mono<CountryRequestDto> countryRequest =
                countryServiceClient.getCountryByNameFromService(countryName);
        CountryRequestDto finland = CountryRequestDto.builder().alpha2Code("FI").capital("Helsinki")
                .flag("https://restcountries.eu/data/fin.svg").name("Finland")
                .population((long) 5491817).build();

        StepVerifier.create(countryRequest).expectNext(finland).expectComplete();
    }

    @Test
    void testRequestGetCountryByNameThatNotExist() {

        String nonExistName = "vietfin";
        Mono<CountryRequestDto> countryRequest =
                countryServiceClient.getCountryByNameFromService(nonExistName);

        StepVerifier.create(countryRequest).expectError(CountryServiceException.class).verify();
    }
}

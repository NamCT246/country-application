package com.nordea.country.integration;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.nordea.country.dto.CountriesResponseDto;
import com.nordea.country.dto.CountryResponseDto;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class WiremockWebClientTest {
    @Value("${country.rest.api.endpoint}")
    private String countryEndpoint;

    private WireMockServer mockServer;

    @Autowired
    private ObjectMapper objectMapper;

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
    void testRemoteService() throws JsonProcessingException {
        CountriesResponseDto finland = new CountriesResponseDto("Finland", "FI");
        CountriesResponseDto vietnam = new CountriesResponseDto("Vietnam", "VN");
        List<CountriesResponseDto> dummyCountryList = new ArrayList<CountriesResponseDto>();

        dummyCountryList.add(finland);
        dummyCountryList.add(vietnam);

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
}

package com.nordea.country.integration;

import com.nordea.country.dto.CountriesRequestDto;
import com.nordea.country.dto.CountryRequestDto;
import com.nordea.country.exceptions.CountryServiceException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * This class would handle calling from external service, by utilizing WebClient interface
 */
@Service
public class CountryServiceClient {

        @Autowired
        private String apiHost;

        @Autowired
        private WebClient client;

        private final String ALL_COUNTRIES_ENDPOINT = "/all/";
        private final String COUNTRY_NAME_ENDPOINT = "/name/";

        public Flux<CountriesRequestDto> getAllCountriesFromService() {

                Flux<CountriesRequestDto> response = client.get()
                                .uri(apiHost + ALL_COUNTRIES_ENDPOINT)
                                .accept(MediaType.APPLICATION_JSON).retrieve()
                                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono
                                                .error(new CountryServiceException(
                                                                "Remote service can't find the resource. Error code: "
                                                                                + clientResponse.statusCode()
                                                                                                .toString())))
                                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono
                                                .error(new CountryServiceException(
                                                                "Remote service is down, error code: "
                                                                                + clientResponse.statusCode()
                                                                                                .toString())))
                                .bodyToFlux(CountriesRequestDto.class);

                return response;
        }

        public Mono<CountryRequestDto> getCountryByNameFromService(String countryName) {
                Mono<CountryRequestDto> response = client.get()
                                .uri(apiHost + COUNTRY_NAME_ENDPOINT + countryName)
                                .accept(MediaType.APPLICATION_JSON).retrieve()
                                .onStatus(HttpStatus::is4xxClientError, clientResponse -> Mono
                                                .error(new CountryServiceException(
                                                                "Remote service can't find the resource. Error code: "
                                                                                + clientResponse.statusCode()
                                                                                                .toString())))
                                .onStatus(HttpStatus::is5xxServerError, clientResponse -> Mono
                                                .error(new CountryServiceException(
                                                                "Remote service is down, error code: "
                                                                                + clientResponse.statusCode()
                                                                                                .toString())))
                                .bodyToMono(CountryRequestDto[].class)
                                .map(countryList -> countryList[0]);

                return response;
        }

}

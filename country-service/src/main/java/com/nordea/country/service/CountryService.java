package com.nordea.country.service;

import java.util.List;
import com.nordea.country.dto.CountriesListResponseDto;
import com.nordea.country.dto.CountriesResponseDto;
import com.nordea.country.dto.CountryResponseDto;
import com.nordea.country.integration.CountryServiceClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class CountryService {

        @Autowired
        private CountryServiceClient client;

        public Mono<CountriesListResponseDto> getAllCountries() {
                return client.getAllCountriesFromService()
                                .map(country -> CountriesResponseDto.builder()
                                                .countryCode(country.getAlpha2Code())
                                                .name(country.getName()).build())
                                .collectList().flatMap(countryList -> Mono
                                                .just(new CountriesListResponseDto(countryList)));

        }

        public Mono<CountryResponseDto> getCountryByName(String countryName) {
                return client.getCountryByNameFromService(countryName)
                                .flatMap(country -> Mono.justOrEmpty(CountryResponseDto.builder()
                                                .name(country.getName())
                                                .capital(country.getCapital())
                                                .countryCode(country.getAlpha2Code())
                                                .flagFileUrl(country.getFlag())
                                                .population(country.getPopulation()).build()));
        }

}

package com.nordea.country.controller;

import com.nordea.country.dto.CountriesListResponseDto;
import com.nordea.country.dto.CountryResponseDto;
import com.nordea.country.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import io.swagger.annotations.Api;
import reactor.core.publisher.Mono;

@Api(tags = {"Get related data to country from external service"})
@RestController
@RequestMapping("/api/countries")
public class CountryController {
    @Autowired
    private CountryService countryService;

    @GetMapping("/")
    public Mono<CountriesListResponseDto> getCountries() {
        return countryService.getAllCountries();
    }

    @GetMapping("/{countryName}")
    public Mono<CountryResponseDto> getCountryByName(@PathVariable String countryName) {
        return countryService.getCountryByName(countryName);
    }
}

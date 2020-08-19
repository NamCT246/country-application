package com.nordea.country.dto;

import java.util.List;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
public class CountriesResponseDto {
    private String name;
    @JsonProperty("country_code")
    private String countryCode;
}

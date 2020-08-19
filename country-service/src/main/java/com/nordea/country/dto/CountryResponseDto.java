package com.nordea.country.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountryResponseDto {
    private String name;
    @JsonProperty("country_code")
    private String countryCode;
    private String capital;
    private Long population;
    @JsonProperty("flag_file_url")
    private String flagFileUrl;
}

package com.nordea.country.dto;

import lombok.Data;

@Data
public class CountriesRequestDto {
    private String name;
    private String alpha2Code;
}

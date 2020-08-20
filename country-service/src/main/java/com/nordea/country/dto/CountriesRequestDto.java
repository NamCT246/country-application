package com.nordea.country.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CountriesRequestDto {
    private String name;
    private String alpha2Code;
}

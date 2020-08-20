package com.nordea.country.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CountryRequestDto {
    private String name;
    private String alpha2Code;
    private String capital;
    private Long population;
    private String flag;
}

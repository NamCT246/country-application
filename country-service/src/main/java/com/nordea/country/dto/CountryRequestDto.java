package com.nordea.country.dto;

import lombok.Data;

@Data
public class CountryRequestDto {
    private String name;
    private String alpha2Code;
    private String capital;
    private Long population;
    private String flag;
}

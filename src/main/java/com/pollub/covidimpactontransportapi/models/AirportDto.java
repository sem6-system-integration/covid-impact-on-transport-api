package com.pollub.covidimpactontransportapi.models;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AirportDto {
    private String icao;
    private String countryCode;
    private String name;
    private String city;

    public AirportDto(String icao, String countryCode, String name, String city) {
        this.icao = icao;
        this.countryCode = countryCode;
        this.name = name;
        this.city = city;
    }
}

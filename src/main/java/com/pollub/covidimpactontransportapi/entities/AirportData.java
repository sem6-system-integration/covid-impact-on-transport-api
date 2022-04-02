package com.pollub.covidimpactontransportapi.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@NoArgsConstructor
public class AirportData {
    @Id
    private String icao;
    private String countryCode;

    public AirportData(String icao, String countryCode) {
        this.icao = icao;
        this.countryCode = countryCode;
    }
}

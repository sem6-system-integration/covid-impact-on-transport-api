package com.pollub.covidimpactontransportapi.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@IdClass(FlightsDataId.class)
@Setter
@Getter
@NoArgsConstructor
public class FlightsData {
    @Id
    private String airportCode;

    @Id
    private int year;

    @Id
    private int month;

    private int flightsCount;

    public FlightsData(String airportCode, int year, int month, int flightsCount) {
        this.airportCode = airportCode;
        this.year = year;
        this.month = month;
        this.flightsCount = flightsCount;
    }
}

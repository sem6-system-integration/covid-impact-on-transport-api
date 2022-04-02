package com.pollub.covidimpactontransportapi.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class FlightsDataId implements Serializable {
    private String airportCode;
    private int year;
    private int month;
}

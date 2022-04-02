package com.pollub.covidimpactontransportapi.entities;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class CovidDataId implements Serializable {
    private String countryCode;
    private Integer year;
    private Integer month;
}

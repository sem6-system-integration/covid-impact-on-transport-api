package com.pollub.covidimpactontransportapi.entities.id_classes;

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

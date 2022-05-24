package com.pollub.covidimpactontransportapi.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Airport {
    @Id
    private String icao;
    private String countryCode;
    private String name;
    private String city;
}

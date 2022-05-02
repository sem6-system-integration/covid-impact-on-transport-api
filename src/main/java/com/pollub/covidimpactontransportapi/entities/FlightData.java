package com.pollub.covidimpactontransportapi.entities;

import com.pollub.covidimpactontransportapi.entities.id_classes.FlightDataId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(FlightDataId.class)
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class FlightData {
    @Id
    private String airportCode;

    @Id
    private int year;

    @Id
    private int month;

    private int flightCount;
}

package com.pollub.covidimpactontransportapi.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyFlightsDataResponse {
    private String airportCode;
    private int year;
    private int month;
    private int flightsCount;
}

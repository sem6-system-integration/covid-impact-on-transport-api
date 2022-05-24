package com.pollub.covidimpactontransportapi.models.responses;

import com.pollub.covidimpactontransportapi.models.AirportDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AirportDataResponse {
    private String countryCode;
    private List<AirportDto> airports;
}

package com.pollub.covidimpactontransportapi.services.airport_service;

import com.pollub.covidimpactontransportapi.dto.AirportDataResponse;

import java.io.IOException;
import java.util.List;

public interface IAirportDataService {
    AirportDataResponse getAirportsByCountryCode(String countryCode) throws IOException, InterruptedException;
}

package com.pollub.covidimpactontransportapi.services.airport_service;

import com.pollub.covidimpactontransportapi.models.responses.AirportDataResponse;

import java.io.IOException;

public interface IAirportDataService {
    AirportDataResponse getAirportsByCountryCode(String countryCode) throws IOException, InterruptedException;
}

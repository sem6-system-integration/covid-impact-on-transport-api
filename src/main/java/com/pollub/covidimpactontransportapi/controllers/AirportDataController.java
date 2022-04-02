package com.pollub.covidimpactontransportapi.controllers;

import com.pollub.covidimpactontransportapi.dto.AirportDataResponse;
import com.pollub.covidimpactontransportapi.services.airport_service.IAirportDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class AirportDataController {
    private final IAirportDataService airportDataService;

    public AirportDataController(IAirportDataService airportDataService) {
        this.airportDataService = airportDataService;
    }

    @GetMapping("/airports/{countryCode}")
    public ResponseEntity<AirportDataResponse> getAirportsByCountryCode(@PathVariable String countryCode) throws IOException, InterruptedException {
        var airportsResponse = airportDataService.getAirportsByCountryCode(countryCode);
        return new ResponseEntity<>(airportsResponse, HttpStatus.OK);
    }
}

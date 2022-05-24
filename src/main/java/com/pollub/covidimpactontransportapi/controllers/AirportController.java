package com.pollub.covidimpactontransportapi.controllers;

import com.pollub.covidimpactontransportapi.models.responses.AirportDataResponse;
import com.pollub.covidimpactontransportapi.services.airport_service.IAirportDataService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/airports")
@PreAuthorize("hasAuthority('USER')")
public class AirportController {
    private final IAirportDataService airportDataService;

    public AirportController(IAirportDataService airportDataService) {
        this.airportDataService = airportDataService;
    }

    @GetMapping("country/{countryCode}")
    @Operation(summary = "Get airports in specified country. Accepts only country code.")
    public ResponseEntity<AirportDataResponse> getAirportsByCountryCode(@PathVariable String countryCode) throws IOException, InterruptedException {
        var airportsResponse = airportDataService.getAirportsByCountryCode(countryCode);
        return new ResponseEntity<>(airportsResponse, HttpStatus.OK);
    }
}

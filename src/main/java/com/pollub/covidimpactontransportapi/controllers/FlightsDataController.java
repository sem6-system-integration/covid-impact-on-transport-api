package com.pollub.covidimpactontransportapi.controllers;

import com.pollub.covidimpactontransportapi.models.responses.MonthlyFlightsDataResponse;
import com.pollub.covidimpactontransportapi.models.responses.YearlyFlightsDataResponse;
import com.pollub.covidimpactontransportapi.services.flights_service.IFlightsDataService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

@RestController
@RequestMapping("/api/flights/airport")
@PreAuthorize("hasAuthority('USER')")
public class FlightsDataController {
    private final IFlightsDataService flightsDataService;

    public FlightsDataController(IFlightsDataService flightsDataService) {
        this.flightsDataService = flightsDataService;
    }

    @GetMapping("{airportCode}/year/{year}/month/{month}")
    @Operation(summary = "Get flights count at a given airport in specified year and month.")
    public ResponseEntity<MonthlyFlightsDataResponse> getFlightCountByAirportCodeInYearInMonth(@PathVariable String airportCode,
                                                                                               @PathVariable int year,
                                                                                               @PathVariable int month) throws IOException, InterruptedException, ParseException {
        var flightsDataResponse = flightsDataService.getFlightsDataByAirportCodeInYearAndInMonth(airportCode, year, month);
        return new ResponseEntity<>(flightsDataResponse, HttpStatus.OK);
    }

    @GetMapping("{airportCode}/year/{year}")
    @Operation(summary = "Get flights count at a given airport in specified year.")
    public ResponseEntity<YearlyFlightsDataResponse> getFlightCountByAirportCodeInYear(@PathVariable String airportCode,
                                                                                       @PathVariable int year) throws IOException, InterruptedException, ParseException {
        var flightsDataResponse = flightsDataService.getFlightsDataByAirportCodeInYear(airportCode, year);
        return new ResponseEntity<>(flightsDataResponse, HttpStatus.OK);
    }
}

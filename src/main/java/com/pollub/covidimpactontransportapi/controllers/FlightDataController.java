package com.pollub.covidimpactontransportapi.controllers;

import com.pollub.covidimpactontransportapi.models.responses.MonthlyFlightDataResponse;
import com.pollub.covidimpactontransportapi.models.responses.YearlyFlightDataResponse;
import com.pollub.covidimpactontransportapi.services.flights_service.IFlightDataService;
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
@PreAuthorize("hasAuthority('STANDARD')")
public class FlightDataController {
    private final IFlightDataService flightDataService;

    public FlightDataController(IFlightDataService flightDataService) {
        this.flightDataService = flightDataService;
    }

    @GetMapping("{airportCode}/year/{year}/month/{month}")
    @Operation(summary = "Get flights count at a given airport in specified year and month.")
    public ResponseEntity<MonthlyFlightDataResponse> getFlightCountByAirportCodeInYearInMonth(@PathVariable String airportCode,
                                                                                              @PathVariable int year,
                                                                                              @PathVariable int month) throws IOException, InterruptedException, ParseException {
        var flightDataResponse = flightDataService.getFlightDataByAirportCodeInYearAndInMonth(airportCode, year, month);
        return new ResponseEntity<>(flightDataResponse, HttpStatus.OK);
    }

    @GetMapping("{airportCode}/year/{year}")
    @Operation(summary = "Get flights count at a given airport in specified year.")
    public ResponseEntity<YearlyFlightDataResponse> getFlightCountByAirportCodeInYear(@PathVariable String airportCode,
                                                                                      @PathVariable int year) throws IOException, InterruptedException, ParseException {
        var flightDataResponse = flightDataService.getFlightDataByAirportCodeInYear(airportCode, year);
        return new ResponseEntity<>(flightDataResponse, HttpStatus.OK);
    }
}

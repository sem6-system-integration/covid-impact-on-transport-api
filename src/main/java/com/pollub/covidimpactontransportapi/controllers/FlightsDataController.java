package com.pollub.covidimpactontransportapi.controllers;

import com.pollub.covidimpactontransportapi.dto.MonthlyFlightsDataResponse;
import com.pollub.covidimpactontransportapi.dto.YearlyFlightsDataResponse;
import com.pollub.covidimpactontransportapi.services.flights_service.IFlightsDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

@RestController
public class FlightsDataController {
    private final IFlightsDataService flightsDataService;

    public FlightsDataController(IFlightsDataService flightsDataService) {
        this.flightsDataService = flightsDataService;
    }

    @GetMapping("/flights/{airportCode}/{year}/{month}")
    public ResponseEntity<MonthlyFlightsDataResponse> getFlightsCountByAirportCodeInYearInMonth(@PathVariable String airportCode,
                                                                                                @PathVariable int year,
                                                                                                @PathVariable int month) throws IOException, InterruptedException, ParseException {
        var flightsDataResponse = flightsDataService.getFlightsDataByAirportCodeInYearInMonth(airportCode, year, month);
        return new ResponseEntity<>(flightsDataResponse, HttpStatus.OK);
    }

    @GetMapping("/flights/{airportCode}/{year}")
    public ResponseEntity<YearlyFlightsDataResponse> getFlightsCountByAirportCodeInYearInMonth(@PathVariable String airportCode,
                                                                                               @PathVariable int year) throws IOException, InterruptedException, ParseException {
        var flightsDataResponse = flightsDataService.getFlightsDataByAirportCodeInYear(airportCode, year);
        return new ResponseEntity<>(flightsDataResponse, HttpStatus.OK);
    }
}

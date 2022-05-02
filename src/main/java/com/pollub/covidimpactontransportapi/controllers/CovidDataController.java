package com.pollub.covidimpactontransportapi.controllers;

import com.pollub.covidimpactontransportapi.dto.MonthlyCovidDataResponse;
import com.pollub.covidimpactontransportapi.dto.YearlyCovidDataResponse;
import com.pollub.covidimpactontransportapi.services.covid_service.ICovidDataService;
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
@RequestMapping("/api/covid")
@PreAuthorize("hasAuthority('USER')")
public class CovidDataController {
    private final ICovidDataService covidDataService;

    public CovidDataController(ICovidDataService covidDataService) {
        this.covidDataService = covidDataService;
    }

    @GetMapping("country/{countryCode}/year/{year}")
    @Operation(summary = "Returns covid data for a country in the specified year.")
    public ResponseEntity<YearlyCovidDataResponse> getCovidCasesInYearByCountry(@PathVariable String countryCode,
                                                                                @PathVariable Integer year) throws ParseException, IOException, InterruptedException {
        var covidDataResponse = covidDataService.getCovidDataByCountryCodeInYear(countryCode, year);
        return new ResponseEntity<>(covidDataResponse, HttpStatus.OK);
    }

    @GetMapping("country/{countryCode}/year/{year}/month/{month}")
    @Operation(summary = "Returns covid data for a country in specified year and month.")
    public ResponseEntity<MonthlyCovidDataResponse> getCovidDataInMonthInYearByCountry(@PathVariable String countryCode,
                                                                                       @PathVariable Integer year,
                                                                                       @PathVariable Integer month) throws ParseException, IOException, InterruptedException {
        var covidDataResponse = covidDataService.getCovidDataByCountryCodeInMonthAndInYear(countryCode, year, month);
        return new ResponseEntity<>(covidDataResponse, HttpStatus.OK);
    }
}

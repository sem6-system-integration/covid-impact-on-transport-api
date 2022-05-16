package com.pollub.covidimpactontransportapi.controllers;

import com.pollub.covidimpactontransportapi.models.responses.MonthlyCovidCasesResponse;
import com.pollub.covidimpactontransportapi.models.responses.MonthlyCovidDeathsResponse;
import com.pollub.covidimpactontransportapi.models.responses.YearlyCovidCasesResponse;
import com.pollub.covidimpactontransportapi.models.responses.YearlyCovidDeathsResponse;
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

    @GetMapping("country/{countryCode}/year/{year}/cases")
    @Operation(summary = "Returns covid cases for a country in the specified year.")
    public ResponseEntity<YearlyCovidCasesResponse> getCovidCasesInYearByCountry(@PathVariable String countryCode,
                                                                                 @PathVariable Integer year) throws ParseException, IOException, InterruptedException {
        var covidCasesResponse = covidDataService.getCovidCasesByCountryCodeInYear(countryCode, year);
        return new ResponseEntity<>(covidCasesResponse, HttpStatus.OK);
    }

    @GetMapping("country/{countryCode}/year/{year}/month/{month}/cases")
    @Operation(summary = "Returns covid cases for a country in specified year and month.")
    public ResponseEntity<MonthlyCovidCasesResponse> getCovidCasesInMonthInYearByCountry(@PathVariable String countryCode,
                                                                                         @PathVariable Integer year,
                                                                                         @PathVariable Integer month) throws ParseException, IOException, InterruptedException {
        var covidCasesResponse = covidDataService.getCovidCasesByCountryCodeInMonthAndInYear(countryCode, year, month);
        return new ResponseEntity<>(covidCasesResponse, HttpStatus.OK);
    }

    @GetMapping("country/{countryCode}/year/{year}/deaths")
    @PreAuthorize("hasAuthority('PREMIUM')")
    @Operation(summary = "Returns covid deaths for a country in the specified year.")
    public ResponseEntity<YearlyCovidDeathsResponse> getCovidDeathsInYearByCountry(@PathVariable String countryCode,
                                                                                   @PathVariable Integer year) throws IOException, InterruptedException {
        var covidDeathsResponse = covidDataService.getCovidDeathsByCountryCodeInYear(countryCode, year);
        return new ResponseEntity<>(covidDeathsResponse, HttpStatus.OK);
    }

    @GetMapping("country/{countryCode}/year/{year}/month/{month}/deaths")
    @PreAuthorize("hasAuthority('PREMIUM')")
    @Operation(summary = "Returns covid deaths for a country in specified year and month.")
    public ResponseEntity<MonthlyCovidDeathsResponse> getCovidDeathsInMonthInYearByCountry(@PathVariable String countryCode,
                                                                                           @PathVariable Integer year,
                                                                                           @PathVariable Integer month) throws IOException, InterruptedException {
        var covidDeathsResponse = covidDataService.getCovidDeathsByCountryCodeInMonthAndInYear(countryCode, year, month);
        return new ResponseEntity<>(covidDeathsResponse, HttpStatus.OK);
    }
}

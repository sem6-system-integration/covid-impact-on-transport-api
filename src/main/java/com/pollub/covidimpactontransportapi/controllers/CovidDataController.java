package com.pollub.covidimpactontransportapi.controllers;

import com.pollub.covidimpactontransportapi.dto.MonthlyCovidDataResponse;
import com.pollub.covidimpactontransportapi.dto.YearlyCovidDataResponse;
import com.pollub.covidimpactontransportapi.services.covid_service.ICovidDataService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

@RestController
public class CovidDataController {
    private final ICovidDataService covidDataService;

    public CovidDataController(ICovidDataService covidDataService) {
        this.covidDataService = covidDataService;
    }

//    @GetMapping("/country/{country}")
//    @Operation(summary = "Get covid data for a country. Accepts country code and country name.")
//    public ResponseEntity<Integer> fetchCovidDataFromCountryToDb(@PathVariable String country) throws IOException, InterruptedException {
//        var numberOfSavedData = covidDataService.fetchCovidDataFromCountryToDb(country);
//        return new ResponseEntity<>(numberOfSavedData, HttpStatus.OK);
//    }

    @GetMapping("/country/{country}/year/{year}")
    @Operation(summary = "Get covid data for a country in a year. Accepts country code and country name.")
    public ResponseEntity<YearlyCovidDataResponse> getCovidCasesInYearByCountry(@PathVariable String country,
                                                                                @PathVariable Integer year) throws ParseException, IOException, InterruptedException {
        var covidDataResponse = covidDataService.getCovidCasesInYearByCountry(country, year);
        return new ResponseEntity<>(covidDataResponse, HttpStatus.OK);
    }

    @GetMapping("/country/{country}/year/{year}/month/{month}")
    @Operation(summary = "Get covid data for a country in a year and month. Accepts country code and country name.")
    public ResponseEntity<MonthlyCovidDataResponse> getCovidDataInMonthInYearByCountry(@PathVariable String country,
                                                                                       @PathVariable Integer year,
                                                                                       @PathVariable Integer month) throws ParseException, IOException, InterruptedException {
        var covidDataResponse = covidDataService.getCovidDataInMonthInYearByCountry(country, year, month);
        return new ResponseEntity<>(covidDataResponse, HttpStatus.OK);
    }
}

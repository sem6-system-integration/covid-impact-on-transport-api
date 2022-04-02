package com.pollub.covidimpactontransportapi.controllers;

import com.pollub.covidimpactontransportapi.dto.CovidDataResponse;
import com.pollub.covidimpactontransportapi.services.covid_service.ICovidDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;

@RestController
public class CovidController {
    private final ICovidDataService covidDataService;

    public CovidController(ICovidDataService covidDataService) {
        this.covidDataService = covidDataService;
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<Integer> saveCovidDataByCountryToDb(@PathVariable String country) throws IOException, InterruptedException {
        var numberOfSavedData = covidDataService.saveCovidDataByCountryToDb(country);
        return new ResponseEntity<>(numberOfSavedData, HttpStatus.OK);
    }

    @GetMapping("/country/{country}/year/{year}")
    public ResponseEntity<CovidDataResponse> getCovidCasesInYearByCountry(@PathVariable String country,
                                                                          @PathVariable Integer year) throws ParseException {
        var covidDataResponse = covidDataService.getCovidCasesInYearByCountry(country, year);
        return new ResponseEntity<>(covidDataResponse, HttpStatus.OK);
    }

    @GetMapping("/country/{country}/year/{year}/month/{month}")
    public ResponseEntity<CovidDataResponse> getCovidDataInMonthInYearByCountry(@PathVariable String country,
                                                                                @PathVariable Integer year,
                                                                                @PathVariable Integer month) throws ParseException {
        var covidDataResponse = covidDataService.getCovidDataInMonthInYearByCountry(country, year, month);
        return new ResponseEntity<>(covidDataResponse, HttpStatus.OK);
    }
}

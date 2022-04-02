package com.pollub.covidimpactontransportapi.controllers;

import com.pollub.covidimpactontransportapi.services.covid_service.ICovidDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
public class CovidController {
    private final ICovidDataService covidDataService;

    public CovidController(ICovidDataService covidDataService) {
        this.covidDataService = covidDataService;
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<Integer> saveCovidDataByCountryToDb(@PathVariable String country) throws IOException, InterruptedException {
        return new ResponseEntity<>(covidDataService.saveCovidDataByCountryToDb(country), HttpStatus.OK);
    }
}

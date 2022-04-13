package com.pollub.covidimpactontransportapi.controllers;

import com.pollub.covidimpactontransportapi.dto.CountryResponse;
import com.pollub.covidimpactontransportapi.services.country_service.ICountryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/country")
@PreAuthorize("hasAuthority('USER')")
public class CountryController {
    private final ICountryService countryService;

    public CountryController(ICountryService countryService) {
        this.countryService = countryService;
    }

    @GetMapping("/")
    public ResponseEntity<List<CountryResponse>> getAllCountries() throws IOException, InterruptedException {
        var countryResponseList = countryService.getAllCountries();
        return new ResponseEntity<>(countryResponseList, HttpStatus.OK);
    }

    @GetMapping("countryCode/{countryCode}")
    @Operation(summary = "Get country by country code")
    public ResponseEntity<CountryResponse> getCountryByCountryCode(@PathVariable String countryCode) throws IOException, InterruptedException {
        var countryResponse = countryService.getCountryByCode(countryCode);
        return new ResponseEntity<>(countryResponse, HttpStatus.OK);
    }

    @GetMapping("countryName/{countryName}")
    @Operation(summary = "Get country by country name")
    public ResponseEntity<CountryResponse> getCountryByCountryName(@PathVariable String countryName) throws IOException, InterruptedException {
        var countryResponse = countryService.getCountryByName(countryName);
        return new ResponseEntity<>(countryResponse, HttpStatus.OK);
    }
}


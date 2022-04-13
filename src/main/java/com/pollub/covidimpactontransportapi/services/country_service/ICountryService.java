package com.pollub.covidimpactontransportapi.services.country_service;

import com.pollub.covidimpactontransportapi.dto.CountryResponse;

import java.io.IOException;
import java.util.List;

public interface ICountryService {
    CountryResponse getCountryByName(String countryName) throws IOException, InterruptedException;

    CountryResponse getCountryByCode(String countryCode) throws IOException, InterruptedException;

    List<CountryResponse> getAllCountries() throws IOException, InterruptedException;
}

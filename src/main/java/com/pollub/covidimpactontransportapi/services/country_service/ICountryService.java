package com.pollub.covidimpactontransportapi.services.country_service;

import com.pollub.covidimpactontransportapi.models.responses.CountryResponse;

import java.io.IOException;
import java.util.List;

public interface ICountryService {
    CountryResponse getCountryByCode(String code) throws IOException, InterruptedException;

    List<CountryResponse> getAllCountries() throws IOException, InterruptedException;
}
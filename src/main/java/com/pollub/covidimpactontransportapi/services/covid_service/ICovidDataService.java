package com.pollub.covidimpactontransportapi.services.covid_service;

import com.pollub.covidimpactontransportapi.dto.CovidDataResponse;

import java.io.IOException;
import java.text.ParseException;

public interface ICovidDataService {
    int saveCovidDataByCountryToDb(String country) throws IOException, InterruptedException;

    CovidDataResponse getCovidCasesInYearByCountry(String country, int year) throws ParseException;

    CovidDataResponse getCovidDataInMonthInYearByCountry(String country, int year, int month) throws ParseException;
}

package com.pollub.covidimpactontransportapi.services.covid_service;

import com.pollub.covidimpactontransportapi.dto.MonthlyCovidDataResponse;
import com.pollub.covidimpactontransportapi.dto.YearlyCovidDataResponse;

import java.io.IOException;
import java.text.ParseException;

public interface ICovidDataService {
    int fetchCovidDataFromCountryToDb(String country) throws IOException, InterruptedException;

    YearlyCovidDataResponse getCovidCasesInYearByCountry(String country, int year) throws ParseException, IOException, InterruptedException;

    MonthlyCovidDataResponse getCovidDataInMonthInYearByCountry(String country, int year, int month) throws ParseException, IOException, InterruptedException;
}

package com.pollub.covidimpactontransportapi.services.covid_service;

import com.pollub.covidimpactontransportapi.models.responses.MonthlyCovidDataResponse;
import com.pollub.covidimpactontransportapi.models.responses.YearlyCovidDataResponse;

import java.io.IOException;
import java.text.ParseException;

public interface ICovidDataService {
    void fetchCovidDataByCountryCodeToDb(String country) throws IOException, InterruptedException;

    YearlyCovidDataResponse getCovidDataByCountryCodeInYear(String country, int year) throws ParseException, IOException, InterruptedException;

    MonthlyCovidDataResponse getCovidDataByCountryCodeInMonthAndInYear(String country, int year, int month) throws ParseException, IOException, InterruptedException;
}

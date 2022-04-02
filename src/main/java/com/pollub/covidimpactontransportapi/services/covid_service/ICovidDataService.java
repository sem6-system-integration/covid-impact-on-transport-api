package com.pollub.covidimpactontransportapi.services.covid_service;

import com.pollub.covidimpactontransportapi.dto.MonthlyCovidDataResponse;
import com.pollub.covidimpactontransportapi.dto.YearlyCovidDataResponse;

import java.io.IOException;
import java.text.ParseException;

public interface ICovidDataService {
    void fetchCovidDataByCountryToDb(String country) throws IOException, InterruptedException;

    YearlyCovidDataResponse getCovidDataByCountryInYear(String country, int year) throws ParseException, IOException, InterruptedException;

    MonthlyCovidDataResponse getCovidDataByCountryInMonthAndInYear(String country, int year, int month) throws ParseException, IOException, InterruptedException;
}

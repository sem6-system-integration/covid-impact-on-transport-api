package com.pollub.covidimpactontransportapi.services.covid_service;

import com.pollub.covidimpactontransportapi.models.responses.MonthlyCovidCasesResponse;
import com.pollub.covidimpactontransportapi.models.responses.MonthlyCovidDeathsResponse;
import com.pollub.covidimpactontransportapi.models.responses.YearlyCovidCasesResponse;
import com.pollub.covidimpactontransportapi.models.responses.YearlyCovidDeathsResponse;

import java.io.IOException;
import java.text.ParseException;

public interface ICovidDataService {
    void fetchCovidCasesByCountryCodeToDb(String country) throws IOException, InterruptedException;

    YearlyCovidCasesResponse getCovidCasesByCountryCodeInYear(String country, int year) throws ParseException, IOException, InterruptedException;

    MonthlyCovidCasesResponse getCovidCasesByCountryCodeInMonthAndInYear(String country, int year, int month) throws ParseException, IOException, InterruptedException;

    YearlyCovidDeathsResponse getCovidDeathsByCountryCodeInYear(String countryCode, int year) throws IOException, InterruptedException;

    MonthlyCovidDeathsResponse getCovidDeathsByCountryCodeInMonthAndInYear(String countryCode, int year, int month) throws IOException, InterruptedException;
}

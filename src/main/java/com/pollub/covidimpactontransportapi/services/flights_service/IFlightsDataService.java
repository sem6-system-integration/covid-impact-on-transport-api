package com.pollub.covidimpactontransportapi.services.flights_service;

import com.pollub.covidimpactontransportapi.models.responses.MonthlyFlightsDataResponse;
import com.pollub.covidimpactontransportapi.models.responses.YearlyFlightsDataResponse;

import java.io.IOException;
import java.text.ParseException;

public interface IFlightsDataService {
    void fetchFlightsDataByAirportCodeInMonthAndInYearToDb(String airportCode, int year, int month) throws IOException, InterruptedException, ParseException;

    MonthlyFlightsDataResponse getFlightsDataByAirportCodeInYearAndInMonth(String airportCode, int year, int month) throws IOException, InterruptedException, ParseException;

    YearlyFlightsDataResponse getFlightsDataByAirportCodeInYear(String airportCode, int year) throws IOException, InterruptedException, ParseException;
}

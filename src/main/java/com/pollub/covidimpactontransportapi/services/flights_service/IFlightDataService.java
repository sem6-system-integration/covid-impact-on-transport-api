package com.pollub.covidimpactontransportapi.services.flights_service;

import com.pollub.covidimpactontransportapi.models.responses.MonthlyFlightDataResponse;
import com.pollub.covidimpactontransportapi.models.responses.YearlyFlightDataResponse;

import java.io.IOException;
import java.text.ParseException;

public interface IFlightDataService {
    void fetchFlightDataByAirportCodeInMonthAndInYearToDb(String airportCode, int year, int month) throws IOException, InterruptedException, ParseException;

    MonthlyFlightDataResponse getFlightDataByAirportCodeInYearAndInMonth(String airportCode, int year, int month) throws IOException, InterruptedException, ParseException;

    YearlyFlightDataResponse getFlightDataByAirportCodeInYear(String airportCode, int year) throws IOException, InterruptedException, ParseException;
}

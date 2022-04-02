package com.pollub.covidimpactontransportapi.services.flights_service;

import com.pollub.covidimpactontransportapi.dto.MonthlyFlightsDataResponse;
import com.pollub.covidimpactontransportapi.dto.YearlyFlightsDataResponse;

import java.io.IOException;
import java.text.ParseException;

public interface IFlightsDataService {
    Integer saveFlightsDataInMonthInYearByAirportCodeToDb(String airportCode, int year, int month) throws IOException, InterruptedException, ParseException;

    MonthlyFlightsDataResponse getFlightsDataByAirportCodeInYearInMonth(String airportCode, int year, int month) throws IOException, InterruptedException, ParseException;

    YearlyFlightsDataResponse getFlightsDataByAirportCodeInYear(String airportCode, int year) throws IOException, InterruptedException, ParseException;
}

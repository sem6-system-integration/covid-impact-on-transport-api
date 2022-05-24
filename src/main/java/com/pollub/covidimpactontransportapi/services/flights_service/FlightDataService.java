package com.pollub.covidimpactontransportapi.services.flights_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollub.covidimpactontransportapi.entities.FlightData;
import com.pollub.covidimpactontransportapi.models.responses.MonthlyFlightDataResponse;
import com.pollub.covidimpactontransportapi.models.responses.YearlyFlightDataResponse;
import com.pollub.covidimpactontransportapi.repositories.IFlightDataRepository;
import com.pollub.covidimpactontransportapi.utils.MyHttpClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class FlightDataService implements IFlightDataService {
    private final String API_URL = "https://opensky-network.org/";
    private final IFlightDataRepository flightDataRepository;

    public FlightDataService(IFlightDataRepository flightDataRepository) {
        this.flightDataRepository = flightDataRepository;
    }

    @Override
    public void fetchFlightDataByAirportCodeInMonthAndInYearToDb(String airportCode, int year, int month) throws IOException, InterruptedException, ParseException {
        airportCode = airportCode.toUpperCase();
        long beginDateEpoch = new SimpleDateFormat("dd-MM-yyyy").parse("01-" + month + "-" + year).getTime() / 1000;
        // check if beginDateEpoch is in the future
        if (beginDateEpoch > System.currentTimeMillis() / 1000)
            return;

        long endDateEpoch = new SimpleDateFormat("dd-MM-yyyy").parse("31-" + month + "-" + year).getTime() / 1000;
        // cap end date to current date
        endDateEpoch = Long.min(endDateEpoch, System.currentTimeMillis() / 1000);

        final var SECONDS_IN_WEEK = 604_800;
        var flightCount = 0;
        var week = 1;
        var weeks = (int) ((endDateEpoch - beginDateEpoch) / SECONDS_IN_WEEK) + 1;
        for (long dateEpoch = beginDateEpoch + SECONDS_IN_WEEK;
             beginDateEpoch != endDateEpoch;
             beginDateEpoch = dateEpoch, dateEpoch = Long.min(dateEpoch + SECONDS_IN_WEEK, endDateEpoch)) {

            System.out.println("Fetching week: " + week + '/' + weeks);
            var response = MyHttpClient.get(API_URL +
                    "api/flights/arrival?airport=" + airportCode +
                    "&begin=" + beginDateEpoch +
                    "&end=" + dateEpoch);
            System.out.println("Fetched week: " + week + '/' + weeks);
            week++;

            var json = response.body();
            var objectMapper = new ObjectMapper();
            var flights = objectMapper.readValue(json, List.class);
            flightCount += flights.size();
            Thread.sleep(150);
        }

        System.out.println("Flights count: " + flightCount);
        flightDataRepository.save(new FlightData(airportCode, year, month, flightCount));
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public MonthlyFlightDataResponse getFlightDataByAirportCodeInYearAndInMonth(String airportCode, int year, int month) throws IOException, InterruptedException, ParseException {
        airportCode = airportCode.toUpperCase();
        var flightData = flightDataRepository.findByAirportCodeAndYearAndMonth(airportCode, year, month);
        if (flightData == null) {
            fetchFlightDataByAirportCodeInMonthAndInYearToDb(airportCode, year, month);
            flightData = flightDataRepository.findByAirportCodeAndYearAndMonth(airportCode, year, month);
            if (flightData == null)
                return new MonthlyFlightDataResponse(airportCode, year, month, 0);
        }

        return new MonthlyFlightDataResponse(airportCode, year, month, flightData.getFlightCount());
    }

    @Override
    public YearlyFlightDataResponse getFlightDataByAirportCodeInYear(String airportCode, int year) throws IOException, InterruptedException, ParseException {
        airportCode = airportCode.toUpperCase();
        var flightCountInYear = 0;
        for (int month = 1; month <= 12; month++) {
            var flightCount = getFlightDataByAirportCodeInYearAndInMonth(airportCode, year, month).getFlightCount();
            flightCountInYear += flightCount;
        }
        return new YearlyFlightDataResponse(airportCode, year, flightCountInYear);
    }
}

package com.pollub.covidimpactontransportapi.services.flights_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollub.covidimpactontransportapi.dto.MonthlyFlightsDataResponse;
import com.pollub.covidimpactontransportapi.dto.YearlyFlightsDataResponse;
import com.pollub.covidimpactontransportapi.entities.FlightsData;
import com.pollub.covidimpactontransportapi.repositories.flights_repository.IFlightsDataRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class FlightsDataService implements IFlightsDataService {
    private final String API_URL = "https://opensky-network.org/";
    private final IFlightsDataRepository flightsDataRepository;

    public FlightsDataService(IFlightsDataRepository flightsDataRepository) {
        this.flightsDataRepository = flightsDataRepository;
    }

    @Override
    public Integer saveFlightsDataInMonthInYearByAirportCodeToDb(String airportCode, int year, int month) throws IOException, InterruptedException, ParseException {
        long beginDateEpoch = new SimpleDateFormat("dd-MM-yyyy").parse("01-" + month + "-" + year).getTime() / 1000;
        // check if beginDateEpoch is in the future
        if (beginDateEpoch > System.currentTimeMillis() / 1000)
            return 0;

        long endDateEpoch = new SimpleDateFormat("dd-MM-yyyy").parse("31-" + month + "-" + year).getTime() / 1000;
        // cap end date to current date
        endDateEpoch = Long.min(endDateEpoch, System.currentTimeMillis() / 1000);

        final var secondsInWeek = 604800;
        var flightsCount = 0;
        for (long dateEpoch = beginDateEpoch + secondsInWeek;
             dateEpoch < endDateEpoch;
             beginDateEpoch = dateEpoch, dateEpoch = Long.min(dateEpoch + secondsInWeek, endDateEpoch)) {
            var uri = URI.create(API_URL +
                    "api/flights/arrival?airport=" + airportCode +
                    "&begin=" + beginDateEpoch +
                    "&end=" + dateEpoch);

            var client = HttpClient.newHttpClient();
            var request = HttpRequest.newBuilder(uri)
                    .GET()
                    .header("accept", "application/json")
                    .build();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            var json = response.body();
            var objectMapper = new ObjectMapper();
            var flights = objectMapper.readValue(json, List.class);
            flightsCount += flights.size();
        }

        flightsDataRepository.save(new FlightsData(airportCode, year, month, flightsCount));
        return flightsCount;
    }

    @Override
    public MonthlyFlightsDataResponse getFlightsDataByAirportCodeInYearInMonth(String airportCode, int year, int month) throws IOException, InterruptedException, ParseException {
        var flightsData = flightsDataRepository.findByAirportCodeAndYearAndMonth(airportCode, year, month);
        if (flightsData == null) {
            saveFlightsDataInMonthInYearByAirportCodeToDb(airportCode, year, month);
            flightsData = flightsDataRepository.findByAirportCodeAndYearAndMonth(airportCode, year, month);
            if (flightsData == null)
                return new MonthlyFlightsDataResponse(airportCode, year, month, 0);
        }

        return new MonthlyFlightsDataResponse(airportCode, year, month, flightsData.getFlightsCount());
    }

    @Override
    public YearlyFlightsDataResponse getFlightsDataByAirportCodeInYear(String airportCode, int year) throws IOException, InterruptedException, ParseException {
        var flightsCountInYear = 0;
        for (int month = 1; month <= 12; month++) {
            var flightsCount= getFlightsDataByAirportCodeInYearInMonth(airportCode, year, month).getFlightsCount();
            flightsCountInYear += flightsCount;
        }
        return new YearlyFlightsDataResponse(airportCode, year, flightsCountInYear);
    }
}

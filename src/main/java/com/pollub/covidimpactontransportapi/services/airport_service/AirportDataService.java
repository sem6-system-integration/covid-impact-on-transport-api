package com.pollub.covidimpactontransportapi.services.airport_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollub.covidimpactontransportapi.dto.AirportDataResponse;
import com.pollub.covidimpactontransportapi.entities.AirportData;
import com.pollub.covidimpactontransportapi.repositories.airports_repository.IAirportDataRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AirportDataService implements IAirportDataService {
    private final String API_URL = "https://raw.githubusercontent.com/mwgg/Airports/master/airports.json";
    private final IAirportDataRepository airportDataRepository;

    public AirportDataService(IAirportDataRepository airportDataRepository) {
        this.airportDataRepository = airportDataRepository;
    }

    private int saveAirportsToDb() throws IOException, InterruptedException {
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(URI.create(API_URL))
                .GET()
                .header("accept", "application/json")
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var json = response.body();
        var objectMapper = new ObjectMapper();
        var airportData = objectMapper.readValue(json, new TypeReference<Map<String, Map<String, String>>>(){}).values();

        var airports = new ArrayList<AirportData>();
        for (Map<String, String> entry : airportData) {
            var airport = new AirportData(entry.get("icao"), entry.get("country"));
            airports.add(airport);
        }

        airportDataRepository.saveAll(airports);
        return airports.size();
    }

    @Override
    public AirportDataResponse getAirportsByCountryCode(String countryCode) throws IOException, InterruptedException {
        List<AirportData> airports = airportDataRepository.findIcaoByCountryCode(countryCode);
        if (airports.isEmpty()) {
            saveAirportsToDb();
            airports = airportDataRepository.findIcaoByCountryCode(countryCode);
        }

        List<String> icaoList = airports.stream().map(AirportData::getIcao).collect(Collectors.toList());
        return new AirportDataResponse(countryCode, icaoList);
    }
}

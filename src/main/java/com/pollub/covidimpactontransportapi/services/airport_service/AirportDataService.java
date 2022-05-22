package com.pollub.covidimpactontransportapi.services.airport_service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollub.covidimpactontransportapi.entities.AirportData;
import com.pollub.covidimpactontransportapi.models.responses.AirportDataResponse;
import com.pollub.covidimpactontransportapi.repositories.IAirportDataRepository;
import com.pollub.covidimpactontransportapi.utils.MyHttpClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
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

    private void fetchAirportsToDbByCountryCode(String countryCode) throws IOException, InterruptedException {
        var response = MyHttpClient.get(API_URL);
        var json = response.body();
        var objectMapper = new ObjectMapper();
        var airportData = objectMapper.readValue(json, new TypeReference<Map<String, Map<String, String>>>() {
        }).values();

        var airports = new ArrayList<AirportData>();
        for (Map<String, String> entry : airportData) {
            if (entry.get("country").equals(countryCode)) {
                var airport = new AirportData(entry.get("icao"), entry.get("country"));
                airports.add(airport);
            }
        }

        airportDataRepository.saveAll(airports);
    }

    @Override
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public AirportDataResponse getAirportsByCountryCode(String countryCode) throws IOException, InterruptedException {
        List<AirportData> airports = airportDataRepository.findAllByCountryCode(countryCode);
        if (airports.isEmpty()) {
            fetchAirportsToDbByCountryCode(countryCode);
            airports = airportDataRepository.findAllByCountryCode(countryCode);
        }

        List<String> icaoList = airports.stream().map(AirportData::getIcao).collect(Collectors.toList());
        return new AirportDataResponse(countryCode, icaoList);
    }
}

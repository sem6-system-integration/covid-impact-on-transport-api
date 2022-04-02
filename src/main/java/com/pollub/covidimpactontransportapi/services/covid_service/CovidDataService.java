package com.pollub.covidimpactontransportapi.services.covid_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollub.covidimpactontransportapi.entities.CovidData;
import com.pollub.covidimpactontransportapi.repositories.covid_repository.ICovidDataRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class CovidDataService implements ICovidDataService {
    private final String API_URL = "https://api.covid19api.com/";
    private final ICovidDataRepository covidDataRepository;

    public CovidDataService(ICovidDataRepository covidDataRepository) {
        this.covidDataRepository = covidDataRepository;
    }

    @Override
    public int saveCovidDataByCountryToDb(String country) throws IOException, InterruptedException {
        var uri = URI.create(API_URL + "dayone/country/" + country);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(uri)
                .GET()
                .header("accept", "application/json")
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var json = response.body();

        var objectMapper = new ObjectMapper();
        List<CovidData> covidDataEntries = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, CovidData.class));
        covidDataRepository.saveAll(covidDataEntries);
        return covidDataEntries.size();
    }
}

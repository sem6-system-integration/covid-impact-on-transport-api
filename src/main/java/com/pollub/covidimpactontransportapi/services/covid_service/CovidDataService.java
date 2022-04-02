package com.pollub.covidimpactontransportapi.services.covid_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollub.covidimpactontransportapi.dto.CovidDataResponse;
import com.pollub.covidimpactontransportapi.entities.CovidData;
import com.pollub.covidimpactontransportapi.repositories.covid_repository.ICovidDataRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

    public CovidDataResponse getCovidCasesInYearByCountry(String country, int year) throws ParseException {
        var beginDate = new SimpleDateFormat("dd-MM-yyyy").parse("01-01-" + year);
        var endDate = new SimpleDateFormat("dd-MM-yyyy").parse("31-12-" + year);
        return getCovidCasesResponseInRange(country, beginDate, endDate);
    }

    public CovidDataResponse getCovidDataInMonthInYearByCountry(String country, int year, int month) throws ParseException {
        var beginDate = new SimpleDateFormat("dd-MM-yyyy").parse("01-" + month + "-" + year);
        var endDate = new SimpleDateFormat("dd-MM-yyyy").parse("31-" + month + "-" + year);
        return getCovidCasesResponseInRange(country, beginDate, endDate);
    }

    private CovidDataResponse getCovidCasesResponseInRange(String country, Date beginDate, Date endDate) {
        var beginData = covidDataRepository.findFirstByCountryAndDateBetweenOrderByDateAsc(country, beginDate, endDate);
        var endData = covidDataRepository.findFirstByCountryAndDateBetweenOrderByDateDesc(country, beginDate, endDate);
        if (beginData == null || endData == null) {
            return new CovidDataResponse(0L, 0L);
        }
        var confirmed = endData.getConfirmed() - beginData.getConfirmed();
        var deaths = endData.getDeaths() - beginData.getDeaths();
        return new CovidDataResponse(confirmed, deaths);
    }
}

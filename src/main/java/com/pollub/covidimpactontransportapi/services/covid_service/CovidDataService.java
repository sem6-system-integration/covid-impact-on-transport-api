package com.pollub.covidimpactontransportapi.services.covid_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollub.covidimpactontransportapi.dto.DailyCovidData;
import com.pollub.covidimpactontransportapi.dto.MonthlyCovidDataResponse;
import com.pollub.covidimpactontransportapi.dto.YearlyCovidDataResponse;
import com.pollub.covidimpactontransportapi.entities.CovidData;
import com.pollub.covidimpactontransportapi.repositories.ICovidDataRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class CovidDataService implements ICovidDataService {
    private final String API_URL = "https://api.covid19api.com/";
    private final ICovidDataRepository covidDataRepository;

    public CovidDataService(ICovidDataRepository covidDataRepository) {
        this.covidDataRepository = covidDataRepository;
    }

    @Override
    public void fetchCovidDataByCountryToDb(String countryNameOrCountryCode) throws IOException, InterruptedException {
        var uri = URI.create(API_URL + "dayone/country/" + countryNameOrCountryCode);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(uri)
                .GET()
                .header("accept", "application/json")
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var json = response.body();

        var objectMapper = new ObjectMapper();
        List<DailyCovidData> dailyCovidData = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, DailyCovidData.class));
        var country = dailyCovidData.get(0).getCountry().toUpperCase();
        var countryCode = dailyCovidData.get(0).getCountryCode().toUpperCase();

        // Map<Year, Map<Month, Pair<Confirmed, Deaths>>>
        Map<Integer, Map<Integer, Pair<Long, Long>>> map = new HashMap<>();
        for (DailyCovidData data : dailyCovidData) {
            var date = data.getDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            var year = calendar.get(Calendar.YEAR);
            var month = calendar.get(Calendar.MONTH) + 1;
            var cases = data.getConfirmed();
            var deaths = data.getDeaths();

            if (map.containsKey(year)) {
                var monthMap = map.get(year);
                monthMap.put(month, Pair.of(cases, deaths));
            } else {
                var monthMap = new HashMap<Integer, Pair<Long, Long>>();
                monthMap.put(month, Pair.of(cases, deaths));
                map.put(year, monthMap);
            }
        }

        List<CovidData> covidDataList = new ArrayList<>();
        for (var entry : map.entrySet()) {
            var year = entry.getKey();
            var monthMap = entry.getValue();
            for (var monthEntry : monthMap.entrySet()) {
                var month = monthEntry.getKey();
                var pair = monthEntry.getValue();
                var confirmed = pair.getFirst();
                var deaths = pair.getSecond();
                var covidData = new CovidData(countryCode, country, year, month, confirmed, deaths);
                covidDataList.add(covidData);
            }
        }

        for (int i = covidDataList.size() - 1; i > 0; i--) {
            covidDataList.get(i).setConfirmed(covidDataList.get(i).getConfirmed() - covidDataList.get(i - 1).getConfirmed());
            covidDataList.get(i).setDeaths(covidDataList.get(i).getDeaths() - covidDataList.get(i - 1).getDeaths());
        }

        covidDataRepository.saveAllAndFlush(covidDataList);
    }

    @Override
    public YearlyCovidDataResponse getCovidDataByCountryInYear(String country, int year) throws IOException, InterruptedException {
        country = country.toUpperCase();
        fetchCovidDataByCountryToDb(country);

        var covidDataList = covidDataRepository.findAllByCountryNameOrCountryCodeAndYear(country, year);
        if (covidDataList == null || covidDataList.isEmpty()) {
            return new YearlyCovidDataResponse(year, 0L, 0L);
        }

        var confirmed = covidDataList.stream().mapToLong(CovidData::getConfirmed).sum();
        var deaths = covidDataList.stream().mapToLong(CovidData::getDeaths).sum();
        return new YearlyCovidDataResponse(year, confirmed, deaths);
    }

    @Override
    public MonthlyCovidDataResponse getCovidDataByCountryInMonthAndInYear(String country, int year, int month) throws IOException, InterruptedException {
        country = country.toUpperCase();
        fetchCovidDataByCountryToDb(country);

        var covidData = covidDataRepository.findFirstByCountryNameOrCountryCodeAndYearAndMonth(country, year, month);
        if (covidData == null) {
            return new MonthlyCovidDataResponse(year, month, 0L, 0L);
        }

        return new MonthlyCovidDataResponse(year, month, covidData.getConfirmed(), covidData.getDeaths());
    }
}

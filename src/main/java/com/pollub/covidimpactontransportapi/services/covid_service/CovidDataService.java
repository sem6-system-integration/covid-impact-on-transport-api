package com.pollub.covidimpactontransportapi.services.covid_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollub.covidimpactontransportapi.dto.DailyCovidData;
import com.pollub.covidimpactontransportapi.dto.MonthlyCovidDataResponse;
import com.pollub.covidimpactontransportapi.dto.YearlyCovidDataResponse;
import com.pollub.covidimpactontransportapi.entities.CovidData;
import com.pollub.covidimpactontransportapi.repositories.covid_repository.ICovidDataRepository;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.text.ParseException;
import java.util.*;

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
        List<DailyCovidData> dailyCovidData = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, DailyCovidData.class));
        country = dailyCovidData.get(0).getCountry().toUpperCase();
        var countryCode = dailyCovidData.get(0).getCountryCode().toUpperCase();

        // Map<Year, Map<Month, Pair<Cases, Deaths>>>
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
                var cases = pair.getFirst();
                var deaths = pair.getSecond();
                var covidData = new CovidData(null, country, countryCode, year, month, cases, deaths);
                covidDataList.add(covidData);
            }
        }

        for (int i = covidDataList.size() - 1; i > 0; i--) {
            covidDataList.get(i).setCases(covidDataList.get(i).getCases() - covidDataList.get(i - 1).getCases());
            covidDataList.get(i).setDeaths(covidDataList.get(i).getDeaths() - covidDataList.get(i - 1).getDeaths());
        }

        covidDataRepository.saveAllAndFlush(covidDataList);
        return dailyCovidData.size();
    }

    @Override
    public YearlyCovidDataResponse getCovidCasesInYearByCountry(String country, int year) throws ParseException, IOException, InterruptedException {
//        country = country.toUpperCase();
//        saveCovidDataByCountryToDb(country);
//        var beginDate = new SimpleDateFormat("dd-MM-yyyy").parse("01-01-" + year);
//        var endDate = new SimpleDateFormat("dd-MM-yyyy").parse("31-12-" + year);
//
//        // assume country is country name
//        var beginData = covidDataRepository.findFirstByCountryAndDateBetweenOrderByDateAsc(country, beginDate, endDate);
//        var endData = covidDataRepository.findFirstByCountryAndDateBetweenOrderByDateDesc(country, beginDate, endDate);
//
//        // assume country is country code
//        if (beginData == null || endData == null) {
//            beginData = covidDataRepository.findFirstByCountryCodeAndDateBetweenOrderByDateAsc(country, beginDate, endDate);
//            endData = covidDataRepository.findFirstByCountryCodeAndDateBetweenOrderByDateDesc(country, beginDate, endDate);
//        }
//
//        // begin date probably before covid
//        if (beginData == null && endData != null)
//            return new YearlyCovidDataResponse(year, endData.getConfirmed(), endData.getDeaths());
//
//        // end date probably after covid
//        if (endData == null)
//            return new YearlyCovidDataResponse(year, 0L, 0L);
//
//        var confirmed = endData.getConfirmed() - beginData.getConfirmed();
//        var deaths = endData.getDeaths() - beginData.getDeaths();
//        return new YearlyCovidDataResponse(year, confirmed, deaths);
        return null;
    }

    @Override
    public MonthlyCovidDataResponse getCovidDataInMonthInYearByCountry(String country, int year, int month) throws ParseException, IOException, InterruptedException {
//        country = country.toUpperCase();
//        saveCovidDataByCountryToDb(country);
//        var beginDate = new SimpleDateFormat("dd-MM-yyyy").parse("01-" + month + "-" + year);
//        var endDate = new SimpleDateFormat("dd-MM-yyyy").parse("31-" + month + "-" + year);
//
//        // assume country is country name
//        var beginData = covidDataRepository.findFirstByCountryAndDateBetweenOrderByDateAsc(country, beginDate, endDate);
//        var endData = covidDataRepository.findFirstByCountryAndDateBetweenOrderByDateDesc(country, beginDate, endDate);
//
//        // assume country is country code
//        if (beginData == null || endData == null) {
//            beginData = covidDataRepository.findFirstByCountryCodeAndDateBetweenOrderByDateAsc(country, beginDate, endDate);
//            endData = covidDataRepository.findFirstByCountryCodeAndDateBetweenOrderByDateDesc(country, beginDate, endDate);
//        }
//
//        // begin date probably before covid
//        if (beginData == null && endData != null)
//            return new MonthlyCovidDataResponse(year, month, endData.getConfirmed(), endData.getDeaths());
//
//        // end date probably after covid
//        if (endData == null)
//            return new MonthlyCovidDataResponse(year, month, 0L, 0L);
//
//        var confirmed = endData.getConfirmed() - beginData.getConfirmed();
//        var deaths = endData.getDeaths() - beginData.getDeaths();
//        return new MonthlyCovidDataResponse(year, month, confirmed, deaths);
        return null;
    }
}

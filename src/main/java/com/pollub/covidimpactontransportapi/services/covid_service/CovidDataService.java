package com.pollub.covidimpactontransportapi.services.covid_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollub.covidimpactontransportapi.dto.DailyCovidData;
import com.pollub.covidimpactontransportapi.dto.MonthlyCovidDataResponse;
import com.pollub.covidimpactontransportapi.dto.YearlyCovidDataResponse;
import com.pollub.covidimpactontransportapi.entities.CovidData;
import com.pollub.covidimpactontransportapi.repositories.ICovidDataRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Calendar;
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
    public void fetchCovidDataByCountryToDb(String countryNameOrCountryCode) throws IOException, InterruptedException {
        var uri = URI.create(API_URL + "total/dayone/country/" + countryNameOrCountryCode);
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(uri)
                .GET()
                .header("accept", "application/json")
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var json = response.body();

        var objectMapper = new ObjectMapper();
        List<DailyCovidData> dailyCovidData = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, DailyCovidData.class));
        if (dailyCovidData.size() == 0) {
            return;
        }

        var country = dailyCovidData.get(0).getCountry().toUpperCase();
        var countryCode = dailyCovidData.get(0).getCountryCode().toUpperCase();

        var prevMonthTotalConfirmed = 0L;
        var prevMonthTotalDeaths = 0L;
        List<CovidData> covidDataList = new ArrayList<>();
        for (DailyCovidData data : dailyCovidData) {
            Date date = data.getDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            var year = calendar.get(Calendar.YEAR);
            var month = calendar.get(Calendar.MONTH) + 1;
            var day = calendar.get(Calendar.DAY_OF_MONTH);

            if (day == calendar.getActualMaximum(Calendar.DATE)) {
                var confirmed = Math.max(data.getConfirmed() - prevMonthTotalConfirmed, 0);
                var deaths = Math.max(data.getDeaths() - prevMonthTotalDeaths,  0);
                prevMonthTotalConfirmed = data.getConfirmed();
                prevMonthTotalDeaths = data.getDeaths();
                covidDataList.add(new CovidData(countryCode, country, year, month, confirmed, deaths));
            }
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

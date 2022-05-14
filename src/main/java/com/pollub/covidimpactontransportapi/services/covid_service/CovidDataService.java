package com.pollub.covidimpactontransportapi.services.covid_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollub.covidimpactontransportapi.entities.CovidData;
import com.pollub.covidimpactontransportapi.models.DailyCovidDataDto;
import com.pollub.covidimpactontransportapi.models.responses.MonthlyCovidCasesResponse;
import com.pollub.covidimpactontransportapi.models.responses.MonthlyCovidDeathsResponse;
import com.pollub.covidimpactontransportapi.models.responses.YearlyCovidCasesResponse;
import com.pollub.covidimpactontransportapi.models.responses.YearlyCovidDeathsResponse;
import com.pollub.covidimpactontransportapi.repositories.ICovidDataRepository;
import com.pollub.covidimpactontransportapi.utils.MyHttpClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
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
    public void fetchCovidCasesByCountryCodeToDb(String countryCode) throws IOException, InterruptedException {
        var response = MyHttpClient.get(API_URL + "total/dayone/country/" + countryCode);
        var json = response.body();

        var objectMapper = new ObjectMapper();
        List<DailyCovidDataDto> dailyCovidDataDtos = objectMapper.readValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, DailyCovidDataDto.class));
        if (dailyCovidDataDtos.size() == 0) return;

        var countryName = dailyCovidDataDtos.get(0).getCountryName().toUpperCase();

        var prevMonthTotalConfirmed = 0L;
        var prevMonthTotalDeaths = 0L;
        List<CovidData> covidDataList = new ArrayList<>();
        for (DailyCovidDataDto data : dailyCovidDataDtos) {
            Date date = data.getDate();
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            var year = calendar.get(Calendar.YEAR);
            var month = calendar.get(Calendar.MONTH) + 1;
            var day = calendar.get(Calendar.DAY_OF_MONTH);

            if (day == calendar.getActualMaximum(Calendar.DATE)) {
                var confirmed = Math.max(data.getConfirmed() - prevMonthTotalConfirmed, 0);
                var deaths = Math.max(data.getDeaths() - prevMonthTotalDeaths, 0);
                prevMonthTotalConfirmed = data.getConfirmed();
                prevMonthTotalDeaths = data.getDeaths();
                covidDataList.add(new CovidData(countryCode, countryName, year, month, confirmed, deaths));
            }
        }

        covidDataRepository.saveAllAndFlush(covidDataList);
    }

    @Override
    public YearlyCovidCasesResponse getCovidCasesByCountryCodeInYear(String countryCode, int year) throws IOException, InterruptedException {
        fetchCovidCasesByCountryCodeToDb(countryCode);

        var covidDataList = covidDataRepository.findAllByCountryCodeAndYear(countryCode, year);
        if (covidDataList == null || covidDataList.isEmpty()) {
            return new YearlyCovidCasesResponse(year, 0L);
        }

        var confirmed = covidDataList.stream().mapToLong(CovidData::getConfirmed).sum();
        return new YearlyCovidCasesResponse(year, confirmed);
    }

    @Override
    public MonthlyCovidCasesResponse getCovidCasesByCountryCodeInMonthAndInYear(String countryCode, int year, int month) throws IOException, InterruptedException {
        fetchCovidCasesByCountryCodeToDb(countryCode);

        var covidData = covidDataRepository.findFirstByCountryCodeAndYearAndMonth(countryCode, year, month);
        if (covidData == null)
            return new MonthlyCovidCasesResponse(year, month, 0L);

        return new MonthlyCovidCasesResponse(year, month, covidData.getConfirmed());
    }

    @Override
    public YearlyCovidDeathsResponse getCovidDeathsByCountryCodeInYear(String countryCode, int year) throws IOException, InterruptedException {
        fetchCovidCasesByCountryCodeToDb(countryCode);

        var covidDataList = covidDataRepository.findAllByCountryCodeAndYear(countryCode, year);
        if (covidDataList == null || covidDataList.isEmpty()) {
            return new YearlyCovidDeathsResponse(year, 0L);
        }

        var deaths = covidDataList.stream().mapToLong(CovidData::getDeaths).sum();
        return new YearlyCovidDeathsResponse(year, deaths);
    }
    @Override
    public MonthlyCovidDeathsResponse getCovidDeathsByCountryCodeInMonthAndInYear(String countryCode, int year, int month) throws IOException, InterruptedException {
        fetchCovidCasesByCountryCodeToDb(countryCode);

        var covidData = covidDataRepository.findFirstByCountryCodeAndYearAndMonth(countryCode, year, month);
        if (covidData == null)
            return new MonthlyCovidDeathsResponse(year, month, 0L);

        return new MonthlyCovidDeathsResponse(year, month, covidData.getDeaths());
    }

}

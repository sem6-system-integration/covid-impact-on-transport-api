package com.pollub.covidimpactontransportapi.services.covid_service;

import java.io.IOException;

public interface ICovidDataService {
    int saveCovidDataByCountryToDb(String country) throws IOException, InterruptedException;
}

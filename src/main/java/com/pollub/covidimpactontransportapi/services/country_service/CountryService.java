package com.pollub.covidimpactontransportapi.services.country_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollub.covidimpactontransportapi.dto.CountryDto;
import com.pollub.covidimpactontransportapi.dto.CountryResponse;
import com.pollub.covidimpactontransportapi.entities.Country;
import com.pollub.covidimpactontransportapi.repositories.ICountryRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

@Service
public class CountryService  implements ICountryService {
    private final String API_URL = "https://api.covid19api.com/";
    private final ICountryRepository countryRepository;

    public CountryService(ICountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    private void fetchCountriesToDb() throws IOException, InterruptedException {
        var uri = URI.create(API_URL + "countries");
        var client = HttpClient.newHttpClient();
        var request = HttpRequest.newBuilder(uri)
                .GET()
                .header("accept", "application/json")
                .build();
        var response = client.send(request, HttpResponse.BodyHandlers.ofString());
        var json = response.body();

        var mapper = new ObjectMapper();
        List<CountryDto> countriesDto = mapper.readValue(json, mapper.getTypeFactory().constructCollectionType(List.class, CountryDto.class));

        var countries = countriesDto.stream()
                .map(countryDto -> new Country(
                        countryDto.getCode().toUpperCase(),
                        countryDto.getName().toUpperCase())
                ).collect(java.util.stream.Collectors.toList());

        countryRepository.saveAll(countries);
    }

    @Override
    public CountryResponse getCountryByName(String countryName) throws IOException, InterruptedException {
        countryName = countryName.toUpperCase();
        var country = countryRepository.findByName(countryName);
        if (country == null) {
            fetchCountriesToDb();
            country = countryRepository.findByName(countryName);
        }
        return new CountryResponse(country.getCode(), country.getName());
    }

    @Override
    public CountryResponse getCountryByCode(String countryCode) throws IOException, InterruptedException {
        countryCode = countryCode.toUpperCase();
        var country = countryRepository.findByCode(countryCode);
        if (country == null) {
            fetchCountriesToDb();
            country = countryRepository.findByCode(countryCode);
        }
        return new CountryResponse(country.getCode(), country.getName());
    }
}

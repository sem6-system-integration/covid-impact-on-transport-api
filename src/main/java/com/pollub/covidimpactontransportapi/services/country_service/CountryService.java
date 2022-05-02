package com.pollub.covidimpactontransportapi.services.country_service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollub.covidimpactontransportapi.entities.Country;
import com.pollub.covidimpactontransportapi.models.CountryDto;
import com.pollub.covidimpactontransportapi.models.responses.CountryResponse;
import com.pollub.covidimpactontransportapi.repositories.ICountryRepository;
import com.pollub.covidimpactontransportapi.utils.MyHttpClient;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class CountryService implements ICountryService {
    private final String API_URL = "https://api.covid19api.com/";
    private final ICountryRepository countryRepository;

    public CountryService(ICountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }

    private void fetchCountriesToDb() throws IOException, InterruptedException {
        var response = MyHttpClient.get(API_URL + "countries");
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
    public CountryResponse getCountryByCode(String countryCode) throws IOException, InterruptedException {
        countryCode = countryCode.toUpperCase();
        var country = countryRepository.findByCode(countryCode);
        if (country == null) {
            fetchCountriesToDb();
            country = countryRepository.findByCode(countryCode);
        }
        return new CountryResponse(country.getCode(), country.getName());
    }

    @Override
    public List<CountryResponse> getAllCountries() throws IOException, InterruptedException {
        var countries = countryRepository.findAllByOrderByName();
        if (countries.isEmpty()) {
            fetchCountriesToDb();
            countries = countryRepository.findAllByOrderByName();
        }
        return countries.stream().map(country -> new CountryResponse(country.getCode(), country.getName())).collect(java.util.stream.Collectors.toList());
    }
}
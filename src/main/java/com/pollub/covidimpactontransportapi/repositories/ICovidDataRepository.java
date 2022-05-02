package com.pollub.covidimpactontransportapi.repositories;

import com.pollub.covidimpactontransportapi.entities.CovidData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICovidDataRepository extends JpaRepository<CovidData, Long> {
    CovidData findFirstByCountryCodeAndYearAndMonth(String countryCode, int year, int month);

    List<CovidData> findAllByCountryCodeAndYear(String countryCode, int year);
}

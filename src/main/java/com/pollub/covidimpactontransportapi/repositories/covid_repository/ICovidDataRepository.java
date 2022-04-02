package com.pollub.covidimpactontransportapi.repositories.covid_repository;

import com.pollub.covidimpactontransportapi.entities.CovidData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICovidDataRepository extends JpaRepository<CovidData, Long> {
    @Query(value = "SELECT * FROM covid_data WHERE (country = ?1 OR country_code = ?1) AND year = ?2 AND month = ?3", nativeQuery = true)
    CovidData findFirstByCountryNameOrCountryCodeAndYearAndMonth(String country, int year, int month);

    @Query(value = "SELECT * FROM covid_data WHERE (country = ?1 OR country_code = ?1) AND year = ?2", nativeQuery = true)
    List<CovidData> findAllByCountryNameOrCountryCodeAndYear(String country, int year);
}

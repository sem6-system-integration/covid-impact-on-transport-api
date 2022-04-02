package com.pollub.covidimpactontransportapi.repositories.covid_repository;

import com.pollub.covidimpactontransportapi.entities.CovidData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface ICovidDataRepository extends JpaRepository<CovidData, Long> {
    CovidData findFirstByCountryAndDateBetweenOrderByDateAsc(String country, Date date, Date date2);

    CovidData findFirstByCountryAndDateBetweenOrderByDateDesc(String country, Date date, Date date2);

    CovidData findFirstByCountryCodeAndDateBetweenOrderByDateAsc(String country, Date date, Date date2);

    CovidData findFirstByCountryCodeAndDateBetweenOrderByDateDesc(String country, Date date, Date date2);
}

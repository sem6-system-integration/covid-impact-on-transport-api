package com.pollub.covidimpactontransportapi.repositories.covid_repository;

import com.pollub.covidimpactontransportapi.entities.CovidData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICovidDataRepository extends JpaRepository<CovidData, Long> {

}

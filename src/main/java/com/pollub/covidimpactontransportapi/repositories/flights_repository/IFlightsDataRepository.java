package com.pollub.covidimpactontransportapi.repositories.flights_repository;

import com.pollub.covidimpactontransportapi.entities.FlightsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface IFlightsDataRepository  extends JpaRepository<FlightsData, Long> {
    FlightsData findByAirportCodeAndYear(String airportCode, int year);

    FlightsData findByAirportCodeAndYearAndMonth(String airportCode, int year, int month);
}

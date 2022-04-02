package com.pollub.covidimpactontransportapi.repositories;

import com.pollub.covidimpactontransportapi.entities.FlightsData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFlightsDataRepository  extends JpaRepository<FlightsData, Long> {

    FlightsData findByAirportCodeAndYearAndMonth(String airportCode, int year, int month);
}

package com.pollub.covidimpactontransportapi.repositories;

import com.pollub.covidimpactontransportapi.entities.FlightData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IFlightDataRepository extends JpaRepository<FlightData, Long> {
    FlightData findByAirportCodeAndYearAndMonth(String airportCode, int year, int month);
}

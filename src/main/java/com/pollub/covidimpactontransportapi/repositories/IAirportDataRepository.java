package com.pollub.covidimpactontransportapi.repositories;

import com.pollub.covidimpactontransportapi.entities.AirportData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface IAirportDataRepository extends JpaRepository<AirportData, String> {
    List<AirportData> findAllByCountryCode(String countryCode);
}
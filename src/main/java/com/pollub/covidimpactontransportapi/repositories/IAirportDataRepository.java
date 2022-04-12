package com.pollub.covidimpactontransportapi.repositories;

import com.pollub.covidimpactontransportapi.entities.AirportData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IAirportDataRepository extends JpaRepository<AirportData, String> {
    List<AirportData> findAllByCountryCode(String countryCode);
}
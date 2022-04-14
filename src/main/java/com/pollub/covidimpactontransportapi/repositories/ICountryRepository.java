package com.pollub.covidimpactontransportapi.repositories;

import com.pollub.covidimpactontransportapi.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ICountryRepository extends JpaRepository<Country, Long> {
    List<Country> findAllByOrderByName();

    Country findByName(String name);

    Country findByCode(String code);
}


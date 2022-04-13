package com.pollub.covidimpactontransportapi.repositories;

import com.pollub.covidimpactontransportapi.entities.Country;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ICountryRepository extends JpaRepository<Country, Long> {
    Country findByName(String name);

    Country findByCode(String code);
}


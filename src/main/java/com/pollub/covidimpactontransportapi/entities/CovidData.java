package com.pollub.covidimpactontransportapi.entities;

import com.pollub.covidimpactontransportapi.entities.id_classes.CovidDataId;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(CovidDataId.class)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CovidData {
    @Id
    private String countryCode;

    private String country;

    @Id
    private Integer year;

    @Id
    private Integer month;

    private Long confirmed;
    private Long deaths;
}

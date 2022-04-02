package com.pollub.covidimpactontransportapi.entities;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@JsonIgnoreProperties({"Province", "City", "CityCode", "Lat", "Lon"})
public class CovidData {
    @Id
    private String id;
    private String country;
    private String countryCode;
    private Long confirmed;
    private Long deaths;
    private Long recovered;
    private Long active;
    private Date date;

    public CovidData(@JsonProperty("ID") String id,
                     @JsonProperty("Country") String country,
                     @JsonProperty("CountryCode") String countryCode,
                     @JsonProperty("Confirmed") Long confirmed,
                     @JsonProperty("Deaths") Long deaths,
                     @JsonProperty("Recovered") Long recovered,
                     @JsonProperty("Active") Long active,
                     @JsonProperty("Date") Date date) {
        this.id = id;
        this.country = country;
        this.countryCode = countryCode;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
        this.active = active;
        this.date = date;
    }
}

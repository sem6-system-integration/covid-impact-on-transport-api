package com.pollub.covidimpactontransportapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@JsonIgnoreProperties({"ID", "Province", "City", "CityCode", "Lat", "Lon"})
public class DailyCovidData {
    private String country;
    private String countryCode;
    private Long confirmed;
    private Long deaths;
    private Long recovered;
    private Long active;
    private Date date;

    public DailyCovidData(@JsonProperty("Country") String country,
                          @JsonProperty("CountryCode") String countryCode,
                          @JsonProperty("Confirmed") Long confirmed,
                          @JsonProperty("Deaths") Long deaths,
                          @JsonProperty("Recovered") Long recovered,
                          @JsonProperty("Active") Long active,
                          @JsonProperty("Date") Date date) {
        this.country = country;
        this.countryCode = countryCode;
        this.confirmed = confirmed;
        this.deaths = deaths;
        this.recovered = recovered;
        this.active = active;
        this.date = date;
    }
}

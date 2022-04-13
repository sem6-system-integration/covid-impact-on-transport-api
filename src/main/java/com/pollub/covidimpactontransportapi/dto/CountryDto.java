package com.pollub.covidimpactontransportapi.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties({"Slug"})
public class CountryDto {
    String code;
    String name;

    public CountryDto(@JsonProperty("ISO2") String code,
                      @JsonProperty("Country") String name) {
        this.code = code;
        this.name = name;
    }
}

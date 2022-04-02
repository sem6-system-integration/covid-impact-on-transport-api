package com.pollub.covidimpactontransportapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class YearlyCovidDataResponse {
    private Integer year;
    private Long confirmed;
    private Long deaths;
}
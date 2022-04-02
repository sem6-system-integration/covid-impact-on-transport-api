package com.pollub.covidimpactontransportapi.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyCovidDataResponse {
    private Integer year;
    private Integer month;
    private Long confirmed;
    private Long deaths;
}
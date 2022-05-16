package com.pollub.covidimpactontransportapi.models.responses;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class YearlyCovidDeathsResponse {
    private Integer year;
    private Long deaths;
}

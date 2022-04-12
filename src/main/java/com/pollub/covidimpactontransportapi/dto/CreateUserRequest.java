package com.pollub.covidimpactontransportapi.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Size;

@Getter
@Setter
public class CreateUserRequest {
    @Size(min = 4, max = 12, message = "length must be between 4 and 12")
    private String username;

    @Size(min = 6, max = 16, message = "length must be between 6 and 16")
    private String password;
}
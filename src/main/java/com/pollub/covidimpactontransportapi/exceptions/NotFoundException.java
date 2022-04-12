package com.pollub.covidimpactontransportapi.exceptions;

public class NotFoundException extends ApiRequestException {
    public NotFoundException(String message) {
        super(message);
    }
}
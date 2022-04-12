package com.pollub.covidimpactontransportapi.exceptions;

public class BadRequestException extends ApiRequestException {
    public BadRequestException(String message) {
        super(message);
    }
}

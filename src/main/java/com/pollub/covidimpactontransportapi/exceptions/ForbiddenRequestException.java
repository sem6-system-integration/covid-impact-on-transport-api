package com.pollub.covidimpactontransportapi.exceptions;

public class ForbiddenRequestException extends ApiRequestException {
    public ForbiddenRequestException(String message) {
        super(message);
    }
}

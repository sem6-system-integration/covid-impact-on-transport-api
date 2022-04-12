package com.pollub.covidimpactontransportapi.handlers;

import com.pollub.covidimpactontransportapi.exceptions.BadRequestException;
import com.pollub.covidimpactontransportapi.exceptions.ForbiddenRequestException;
import com.pollub.covidimpactontransportapi.exceptions.NotFoundException;
import lombok.Getter;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class CustomRestExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    @NonNull
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatus status,
                                                                  @NonNull WebRequest request) {
        List<String> errors = new ArrayList<>();
        for (FieldError error : ex.getBindingResult().getFieldErrors())
            errors.add(error.getField() + ": " + error.getDefaultMessage());

        for (ObjectError error : ex.getBindingResult().getGlobalErrors())
            errors.add(error.getObjectName() + ": " + error.getDefaultMessage());

        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, ex.getLocalizedMessage(), errors);
        return handleExceptionInternal(ex, apiError, headers, apiError.getStatus(), request);
    }

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    protected ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        var msg = ex.getLocalizedMessage();
        ApiError apiError = new ApiError(HttpStatus.NOT_FOUND, msg, msg);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(), request);
    }

    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    protected ResponseEntity<Object> handleBadRequestException(BadRequestException ex, WebRequest request) {
        var msg = ex.getLocalizedMessage();
        ApiError apiError = new ApiError(HttpStatus.BAD_REQUEST, msg, msg);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(), request);
    }

    @ExceptionHandler(ForbiddenRequestException.class)
    @ResponseStatus(HttpStatus.FORBIDDEN)
    protected ResponseEntity<Object> handleForbiddenRequestException(ForbiddenRequestException ex, WebRequest request) {
        var msg = ex.getLocalizedMessage();
        ApiError apiError = new ApiError(HttpStatus.FORBIDDEN, msg, msg);
        return handleExceptionInternal(ex, apiError, new HttpHeaders(), apiError.getStatus(), request);
    }
}

@Getter
class ApiError {

    private final HttpStatus status;
    private final String message;
    private final List<String> errors;

    public ApiError(HttpStatus status, String message, List<String> errors) {
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ApiError(HttpStatus status, String message, String error) {
        this.status = status;
        this.message = message;
        errors = List.of(error);
    }
}
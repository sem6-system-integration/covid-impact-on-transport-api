package com.pollub.covidimpactontransportapi.services.user_service;

import com.pollub.covidimpactontransportapi.entities.AppUser;
import com.pollub.covidimpactontransportapi.exceptions.BadRequestException;
import com.pollub.covidimpactontransportapi.exceptions.ForbiddenRequestException;
import com.pollub.covidimpactontransportapi.exceptions.NotFoundException;
import com.pollub.covidimpactontransportapi.models.requests.CreateUserRequest;

import java.util.List;

public interface IUserService {
    Long getAuthId();

    List<AppUser> findAllUsers();

    AppUser findUserById(Long id) throws NotFoundException;

    AppUser createUser(CreateUserRequest request) throws BadRequestException;

    void deleteUserById(Long id) throws NotFoundException, BadRequestException, ForbiddenRequestException;
}

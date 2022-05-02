package com.pollub.covidimpactontransportapi.controllers;

import com.pollub.covidimpactontransportapi.entities.AppUser;
import com.pollub.covidimpactontransportapi.exceptions.BadRequestException;
import com.pollub.covidimpactontransportapi.exceptions.ForbiddenRequestException;
import com.pollub.covidimpactontransportapi.exceptions.NotFoundException;
import com.pollub.covidimpactontransportapi.models.requests.CreateUserRequest;
import com.pollub.covidimpactontransportapi.services.user_service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    final IUserService userService;

    public UserController(IUserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @Operation(summary = "Returns all users. Only available for admin.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<AppUser>> getAllUsers() {
        var users = userService.findAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @GetMapping("{id}")
    @Operation(summary = "Returns a user by id. Only available for admin.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<AppUser> getUserById(@PathVariable Long id) throws NotFoundException {
        var user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("auth")
    @Operation(summary = "Returns authenticated user.")
    public ResponseEntity<AppUser> getAuthenticatedUser() throws NotFoundException {
        var id = userService.getAuthId();
        var user = userService.findUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping()
    @SecurityRequirements
    @Operation(summary = "Creates a new user.")
    public ResponseEntity<AppUser> createUser(@Valid @RequestBody CreateUserRequest request) throws BadRequestException {
        var user = userService.createUser(request);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @DeleteMapping("auth")
    @Operation(summary = "Deletes authenticated user.")
    public ResponseEntity<Void> deleteAuthenticatedUser() throws NotFoundException, BadRequestException, ForbiddenRequestException {
        var id = userService.getAuthId();
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
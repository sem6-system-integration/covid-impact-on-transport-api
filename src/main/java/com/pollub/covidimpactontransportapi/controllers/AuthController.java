package com.pollub.covidimpactontransportapi.controllers;


import com.pollub.covidimpactontransportapi.models.requests.CreateUserRequest;
import com.pollub.covidimpactontransportapi.services.user_service.IUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    final IUserService userService;

    public AuthController(IUserService userService) {
        this.userService = userService;
    }

    @PostMapping("login")
    @SecurityRequirements // remove padlock from endpoint
    @Operation(summary = "Authenticates user and returns JWT token upon success.")
    public void login(@RequestBody CreateUserRequest request) {
        // Only for swagger endpoint
        // Logic is handled elsewhere
    }
}

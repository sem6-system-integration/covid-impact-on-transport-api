package com.pollub.covidimpactontransportapi.auth;

public class SecurityConstants {
    public static final String SECRET = "mBxhqjgPMmffkZfzOGIAFbXbrulJfPaO";
    public static final Long EXPIRATION_TIME = 900_000L; // 15 min
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/users";
}

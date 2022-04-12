package com.pollub.covidimpactontransportapi.auth;

public class SecurityConstants {
    public static final String SECRET = "mBxhqjgPMmffkZfzOGIAFbXbrulJfPaO";
    public static final Long EXPIRATION_TIME = 604_800_000L; // 7 days
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String HEADER_STRING = "Authorization";
    public static final String SIGN_UP_URL = "/api/users";
}

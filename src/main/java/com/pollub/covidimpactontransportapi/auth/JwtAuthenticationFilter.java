package com.pollub.covidimpactontransportapi.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.pollub.covidimpactontransportapi.entities.AppUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

import static com.pollub.covidimpactontransportapi.auth.SecurityConstants.EXPIRATION_TIME;
import static com.pollub.covidimpactontransportapi.auth.SecurityConstants.SECRET;


public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    AuthenticationManager authenticationManager;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/api/auth/login");
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            AppUser appUser = new ObjectMapper().readValue(request.getInputStream(), AppUser.class);

            return authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            appUser.getUsername(),
                            appUser.getPassword(),
                            appUser.getAuthorities()
                    )
            );
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult) throws IOException {
        AppUser appUser = (AppUser) authResult.getPrincipal();
        String token = JWT.create()
                .withSubject(String.valueOf(appUser.getId()))
                .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .withClaim("username", appUser.getUsername())
                .withArrayClaim("authorities", appUser.getAuthorities().stream().map(GrantedAuthority::getAuthority).toArray(String[]::new))
                .sign(Algorithm.HMAC512(SECRET.getBytes()));

        String body = appUser.getUsername() + " " + token;

        response.setContentType("text/plain");
        response.getWriter().write(body);
        response.getWriter().flush();
    }
}

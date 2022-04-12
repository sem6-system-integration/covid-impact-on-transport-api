package com.pollub.covidimpactontransportapi.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.SignatureVerificationException;
import com.pollub.covidimpactontransportapi.entities.AppUser;
import com.pollub.covidimpactontransportapi.exceptions.NotFoundException;
import com.pollub.covidimpactontransportapi.services.user_service.IUserService;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.pollub.covidimpactontransportapi.auth.SecurityConstants.*;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

    final private IUserService userService;

    public JwtAuthorizationFilter(AuthenticationManager authenticationManager, IUserService userService) {
        super(authenticationManager);
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {
        String header = request.getHeader(HEADER_STRING);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            SecurityContextHolder.getContext().setAuthentication(null);
            chain.doFilter(request, response);
            return;
        }

        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
    }

    // Reads the JWT from the Authorization header, and then uses JWT to validate the token
    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        String token = request.getHeader(HEADER_STRING);
        if (token == null)
            return null;

        try {
            // parse the token.
            String subject = JWT.require(Algorithm.HMAC512(SECRET.getBytes()))
                    .build()
                    .verify(token.replace(TOKEN_PREFIX, ""))
                    .getSubject();
            if (subject == null)
                return null;

            AppUser appUser = userService.findUserById(Long.parseLong(subject));
            var authorities = appUser.getAuthorities();
            return new UsernamePasswordAuthenticationToken(appUser.getId(), null, authorities);
        } catch (SignatureVerificationException | NotFoundException e) {
            return null;
        }
    }
}

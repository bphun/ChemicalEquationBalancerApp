package com.bphan.ChemicalEquationBalancerApi.auth.security;

import java.io.IOException;
import java.security.Key;
import java.sql.Date;
import java.util.Collections;

import javax.crypto.spec.SecretKeySpec;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.DatatypeConverter;

import com.bphan.ChemicalEquationBalancerApi.auth.models.AuthenticationResponse;
import com.bphan.ChemicalEquationBalancerApi.common.auth.JwtConfig;
import com.bphan.ChemicalEquationBalancerApi.common.auth.UserCredentials;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtUsernameAndPasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authManager;

    private final JwtConfig jwtConfig;

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public JwtUsernameAndPasswordAuthenticationFilter(AuthenticationManager authManager, JwtConfig jwtConfig) {
        this.authManager = authManager;
        this.jwtConfig = jwtConfig;

        this.setRequiresAuthenticationRequestMatcher(new AntPathRequestMatcher(jwtConfig.getUri(), "POST"));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        try {
            UserCredentials creds = new ObjectMapper().readValue(request.getInputStream(), UserCredentials.class);
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(creds.getUsername(),
                    creds.getPassword(), Collections.emptyList());

            return authManager.authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
            Authentication auth) throws IOException, ServletException {
        Long now = System.currentTimeMillis();
        // String token = Jwts.builder().setSubject(auth.getName())
        //         .claim("authorities",
        //                 auth.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
        //         .setIssuedAt(new Date(now)).setExpiration(new Date(now + jwtConfig.getExpiration() * 1000L))
        //         .signWith(SignatureAlgorithm.HS512, jwtConfig.getSecret().getBytes()).compact();

        String token = buildJwt(auth.getName(), jwtConfig.getExpiration());

        response.addHeader(jwtConfig.getHeader(), jwtConfig.getPrefix() + token);
        response.setContentType("application/json");

        AuthenticationResponse responseBody = new AuthenticationResponse("success", "", token);

        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
        response.flushBuffer();
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException failed) throws IOException, ServletException {
        response.setStatus(403);
        response.setContentType("application/json");

        AuthenticationResponse responseBody = new AuthenticationResponse("error", failed.getLocalizedMessage());

        response.getWriter().write(objectMapper.writeValueAsString(responseBody));
        response.flushBuffer();
    }

    private String buildJwt(String subject, long ttlMillis) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        long nowMillis = System.currentTimeMillis();
        Date now = new Date(nowMillis);
        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(jwtConfig.getSecret());
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder().setIssuedAt(now).setSubject(subject).signWith(signatureAlgorithm,
                signingKey);

        if (ttlMillis > 0) {
            long expMillis = nowMillis + ttlMillis;
            Date exp = new Date(expMillis);
            builder.setExpiration(exp);
        }

        return builder.compact();
    }
}
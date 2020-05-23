package com.bphan.ChemicalEquationBalancerApi.common.auth;

import org.springframework.beans.factory.annotation.Value;

public class JwtConfig {
    @Value("${security.jwt.uri:/auth/**}")
    private String uri;

    @Value("${security.jwt.header:Authorization}")
    private String header;

    @Value("${security.jwt.prefix:Bearer }")
    private String prefix;

    @Value("${security.jwt.expiration:#{24*60*60}}")
    private long expiration;

    @Value("${security.jwt.secret:JwtSecretKey}")
    private String secret;

    public String getUri() {
        return this.uri;
    }

    public String getHeader() {
        return this.header;
    }

    public String getPrefix() {
        return this.prefix;
    }

    public long getExpiration() {
        return this.expiration;
    }

    public String getSecret() {
        return this.secret;
    }
}
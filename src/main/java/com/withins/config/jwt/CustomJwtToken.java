package com.withins.config.jwt;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import java.util.Collection;

@Getter
public class CustomJwtToken extends JwtAuthenticationToken {

    private final Long memberId;

    public CustomJwtToken(Jwt jwt, Collection<? extends GrantedAuthority> authorities) {
        super(jwt, authorities);
        this.memberId = Long.valueOf(jwt.getClaimAsString("memberId"));
    }

}
package com.withins.config.jwt;

import com.withins.config.oauth2.PrincipalDetails;
import com.withins.config.oauth2.PrincipalDetailsService;
import com.withins.config.oauth2.PrincipalOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.Collection;

public class CustomJwtAuthenticationConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    private final JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();

    // JWT -> PrincipalDetails 로 변환 할 수 있음 그런데 아직 적용 안함
//    private final PrincipalDetailsService principalDetailsService;
//    private final PrincipalOAuth2UserService principalOAuth2UserService;

    @Override
    public final AbstractAuthenticationToken convert(Jwt jwt) {
        AbstractAuthenticationToken token = jwtAuthenticationConverter.convert(jwt);
        Collection<GrantedAuthority> authorities = token.getAuthorities();
        return new CustomJwtToken(jwt, authorities);
//        return new UsernamePasswordAuthenticationToken()
    }

}

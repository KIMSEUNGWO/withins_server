package com.withins.config.jwt;

import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.boot.web.server.Cookie.SameSite;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private final SecretKey secretKey;
    private final int JWT_ACCESS_TIME = 10;
    private final int JWT_REFRESH_TIME = 60 * 60 * 24;

//    public String generateAccessToken(TokenId tokenId, long memberId,
//                                      Collection<? extends GrantedAuthority> authorities) {
//        return createToken(tokenId, memberId, authorities, JWT_ACCESS_TIME);
//    }
//    public String generateRefreshToken(TokenId tokenId, long memberId,
//                                       Collection<? extends GrantedAuthority> authorities) {
//        return createToken(tokenId, memberId, authorities, JWT_REFRESH_TIME);
//    }

    public ResponseCookie generateAccessToken(TokenId tokenId, long memberId,
                                      Collection<? extends GrantedAuthority> authorities) {
        String token = createToken(tokenId, memberId, authorities, JWT_ACCESS_TIME);
        return createCookie("accessToken", token, JWT_ACCESS_TIME, "/", SameSite.STRICT);
    }

    public ResponseCookie generateRefreshToken(TokenId tokenId, long memberId,
                                              Collection<? extends GrantedAuthority> authorities) {
        String token = createToken(tokenId, memberId, authorities, JWT_REFRESH_TIME);
        return createCookie("refreshToken", token, JWT_REFRESH_TIME, "/auth/refresh", SameSite.STRICT);
    }

    private ResponseCookie createCookie(String cookieName, String token, int expiration, String path, SameSite sameSite) {
        return ResponseCookie.from(cookieName, token)
            .path(path)
            .sameSite(sameSite.attributeValue())
            .httpOnly(true)
            .secure(true)
            .maxAge(expiration)
            .build();
    }
    // 토큰 생성
    private String createToken(TokenId tokenId,
                               long memberId,
                               Collection<? extends GrantedAuthority> authorities,
                               int expirationPeriod) {
        Date now = new Date(System.currentTimeMillis());

        return Jwts.builder()
            .header()
            .keyId(UUID.randomUUID().toString())
            .add("typ", "JWT")
            .and()
            .issuer("TEST_JWT")
            .claim("iss", tokenId.provider())
            .claim("sub", tokenId.providerId())
            .claim("memberId", memberId)
            .claim("scope", authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList())
            .issuedAt(now)
            .expiration(new Date(now.getTime() + expirationPeriod * 1000L))
            .signWith(secretKey)
            .compact();
    }

}

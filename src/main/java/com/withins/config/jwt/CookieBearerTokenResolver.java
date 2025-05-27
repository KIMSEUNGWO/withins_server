package com.withins.config.jwt;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;

public class CookieBearerTokenResolver implements BearerTokenResolver {

    private static final String COOKIE_ACCESS_TOKEN = "accessToken";
    private static final String COOKIE_REFRESH_TOKEN = "refreshToken";
    private static final String AUTHORIZATION_HEADER = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";

    @Override
    public String resolve(HttpServletRequest request) {

        // 🎯 1. Authorization 헤더에서 먼저 확인 (기존 방식 유지)
        String authorizationHeaderToken = resolveFromAuthorizationHeader(request);
        if (authorizationHeaderToken != null) {
            return authorizationHeaderToken;
        }

        // 🎯 2. Cookie에서 토큰 확인 (새로운 방식)
        return resolveFromCookie(request);
    }

    private String resolveFromAuthorizationHeader(HttpServletRequest request) {
        String authorization = request.getHeader(AUTHORIZATION_HEADER);
        if (authorization != null && authorization.startsWith(BEARER_PREFIX)) {
            return authorization.substring(BEARER_PREFIX.length());
        }
        return null;
    }

    private String resolveFromCookie(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (COOKIE_ACCESS_TOKEN.equals(cookie.getName()) || COOKIE_REFRESH_TOKEN.equals(cookie.getName())) {
                    String token = cookie.getValue();
                    return token != null && !token.trim().isEmpty() ? token : null;
                }
            }
        }
        return null;
    }
}

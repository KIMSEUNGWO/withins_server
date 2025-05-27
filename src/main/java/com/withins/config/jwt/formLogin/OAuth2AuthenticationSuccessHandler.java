package com.withins.config.jwt.formLogin;

import com.withins.config.jwt.JwtTokenProvider;
import com.withins.config.jwt.TokenId;
import com.withins.config.oauth2.PrincipalDetails;
import com.withins.entity.Member;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider tokenProvider;

    public OAuth2AuthenticationSuccessHandler(JwtTokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        PrincipalDetails principalDetails = (PrincipalDetails) authentication.getPrincipal();

        Member member = principalDetails.getMember();
        TokenId tokenId = new TokenId(member.getSocialLoginInfo().getProvider().name(), member.getUsername());

        // JWT 토큰 생성
        var accessToken = tokenProvider.generateAccessToken(tokenId, member.getId(), authentication.getAuthorities());
        var refreshToken = tokenProvider.generateRefreshToken(tokenId, member.getId(), authentication.getAuthorities());

        response.addHeader(HttpHeaders.SET_COOKIE, accessToken.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());

        response.sendRedirect("/login/success");
    }
}

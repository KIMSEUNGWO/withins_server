package com.withins.config.jwt.formLogin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withins.config.jwt.JwtTokenProvider;
import com.withins.config.jwt.TokenId;
import com.withins.config.oauth2.PrincipalDetails;
import com.withins.dto.LoginResponse;
import com.withins.entity.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.OAuth2LoginAuthenticationFilter;

import java.io.IOException;

public class OAuth2AuthenticationFilter extends OAuth2LoginAuthenticationFilter {

    private final JwtTokenProvider tokenProvider;
    private final ObjectMapper objectMapper;

    public OAuth2AuthenticationFilter(ClientRegistrationRepository clientRegistrationRepository,
                                      OAuth2AuthorizedClientService authorizedClientService,
                                      JwtTokenProvider tokenProvider,
                                      ObjectMapper objectMapper) {
        super(clientRegistrationRepository, authorizedClientService);
        this.tokenProvider = tokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        System.out.println("OAuth2AuthenticationFilter.successfulAuthentication");
//        super.successfulAuthentication(request, response, chain, authResult);

        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        Member member = principalDetails.getMember();
        TokenId tokenId = new TokenId(member.getSocialLoginInfo().getProvider().name(), member.getUsername());

        // JWT 토큰 생성
        var accessToken = tokenProvider.generateAccessToken(tokenId, member.getId(), authResult.getAuthorities());
        var refreshToken = tokenProvider.generateRefreshToken(tokenId, member.getId(), authResult.getAuthorities());

        response.addHeader(HttpHeaders.SET_COOKIE, accessToken.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());

        response.sendRedirect("/login/success");


    }
}

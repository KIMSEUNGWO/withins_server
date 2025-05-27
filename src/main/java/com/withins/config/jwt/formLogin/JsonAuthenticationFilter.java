package com.withins.config.jwt.formLogin;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withins.config.jwt.JwtTokenProvider;
import com.withins.config.jwt.TokenId;
import com.withins.config.oauth2.PrincipalDetails;
import com.withins.dto.LoginRequest;
import com.withins.dto.LoginResponse;
import com.withins.entity.Member;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JsonAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider tokenProvider;

    public JsonAuthenticationFilter(ObjectMapper objectMapper, JwtTokenProvider tokenProvider) {
        super();
        this.setFilterProcessesUrl("/api/login");
        this.objectMapper = objectMapper;
        this.tokenProvider = tokenProvider;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
                                                HttpServletResponse response) throws AuthenticationException {
        System.out.println("JsonAuthenticationFilter.attemptAuthentication");
        try {
            // JSON 요청 파싱
            LoginRequest loginRequest = objectMapper.readValue(request.getInputStream(), LoginRequest.class);

            // 인증 객체 생성
            UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequest.username(),
                loginRequest.password()
            );

            // AuthenticationManager 에 인증 위임
            return getAuthenticationManager().authenticate(authToken);

        } catch (IOException e) {
            throw new RuntimeException("Failed to parse authentication request", e);
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        PrincipalDetails principalDetails = (PrincipalDetails) authResult.getPrincipal();

        Member member = principalDetails.getMember();
        TokenId tokenId = new TokenId("LOCAL", member.getUsername());

        // JWT 토큰 생성
        var accessToken = tokenProvider.generateAccessToken(tokenId, member.getId(), authResult.getAuthorities());
        var refreshToken = tokenProvider.generateRefreshToken(tokenId, member.getId(), authResult.getAuthorities());

        response.addHeader(HttpHeaders.SET_COOKIE, accessToken.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException failed) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("message", "아이디 또는 비밀번호가 틀렸습니다.");

        objectMapper.writeValue(response.getOutputStream(), errorResponse);
    }
}
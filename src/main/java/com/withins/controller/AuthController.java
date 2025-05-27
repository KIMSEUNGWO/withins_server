package com.withins.controller;

import com.withins.config.jwt.CustomJwtToken;
import com.withins.config.jwt.JwtTokenProvider;
import com.withins.config.jwt.TokenId;
import com.withins.config.oauth2.PrincipalDetails;
import com.withins.dto.LoginResponse;
import com.withins.config.jwt.annotation.JwtToken;
import com.withins.entity.Member;
import com.withins.service.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/refresh")
    public ResponseEntity<Void> refreshToken(@JwtToken CustomJwtToken jwtToken, HttpServletResponse response) throws IOException {
        System.out.println("AuthController.refreshToken");
        authService.refresh(jwtToken.getMemberId(), response);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(@JwtToken CustomJwtToken jwtToken) {
        System.out.println("AuthController.test");
        return ResponseEntity.ok(jwtToken.getName());
    }
}

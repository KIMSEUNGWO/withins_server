package com.withins.service;

import com.withins.config.jwt.JwtTokenProvider;
import com.withins.config.jwt.TokenId;
import com.withins.dto.LoginResponse;
import com.withins.entity.Member;
import com.withins.jparepository.MemberJpaRepository;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberJpaRepository  memberJpaRepository;
    private final JwtTokenProvider tokenProvider;

    @Transactional(readOnly = true)
    public void refresh(Long memberId, HttpServletResponse response) throws IOException {
        Optional<Member> findMember = memberJpaRepository.findById(memberId);
        if (findMember.isEmpty()) return;

        Member member = findMember.get();
        TokenId tokenId = new TokenId(getProvider(member), member.getUsername());

        var authorities = List.of(new SimpleGrantedAuthority(member.getRole().getRoleName()));

        // JWT 토큰 생성
        var accessToken = tokenProvider.generateAccessToken(tokenId, member.getId(), authorities);
        var refreshToken = tokenProvider.generateRefreshToken(tokenId, member.getId(), authorities);

        // JSON 응답 생성
        response.addHeader(HttpHeaders.SET_COOKIE, accessToken.toString());
        response.addHeader(HttpHeaders.SET_COOKIE, refreshToken.toString());
    }

    private String getProvider(Member member) {
        if (member.getSocialLoginInfo() == null) {
            return "LOCAL";
        }
        return member.getSocialLoginInfo().getProvider().name();
    }

}

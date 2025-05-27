package com.withins.controller;

import com.withins.config.jwt.CustomJwtToken;
import com.withins.config.jwt.annotation.JwtToken;
import com.withins.config.oauth2.PrincipalDetails;
import com.withins.dto.MemberInfoRequest;
import com.withins.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/api/user")
    public ResponseEntity<MemberInfoRequest> getMemberInfo(@JwtToken CustomJwtToken jwtToken) {
        System.out.println("MemberController.getMemberInfo");
        MemberInfoRequest memberInfo = memberService.getMemberInfo(jwtToken.getMemberId());
        return ResponseEntity.ok(memberInfo);
    }


}

package com.withins.controller;

import com.withins.config.oauth2.PrincipalDetails;
import com.withins.dto.MemberInfoRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/api/login/success")
    public ResponseEntity<MemberInfoRequest> getMemberInfo(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        String nickname = principalDetails.getMember().getNickname();
        return ResponseEntity.ok(new MemberInfoRequest(nickname));
    }


}

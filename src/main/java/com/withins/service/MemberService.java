package com.withins.service;

import com.withins.dto.MemberInfoRequest;
import com.withins.jparepository.MemberJpaRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class MemberService {

    private final MemberJpaRepository memberJpaRepository;

    public MemberInfoRequest getMemberInfo(Long memberId) {
        return memberJpaRepository.findById(memberId)
            .map(member -> new MemberInfoRequest(member.getNickname()))
            .orElseThrow(()-> new EntityNotFoundException("존재하지 않는 회원입니다."));
    }
}

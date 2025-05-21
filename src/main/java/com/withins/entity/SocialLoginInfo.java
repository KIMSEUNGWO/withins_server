package com.withins.entity;

import com.withins.enums.Provider;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Embeddable
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class SocialLoginInfo {

    @Enumerated(EnumType.STRING)
    private Provider provider;
    private String providerId;
}

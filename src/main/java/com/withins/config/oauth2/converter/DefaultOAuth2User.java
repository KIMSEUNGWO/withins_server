package com.withins.config.oauth2.converter;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public final class DefaultOAuth2User {
    private final String providerId;
    private final String email;
    private final String name;


    public interface Builder {
        String providerId();
        String email();
        String name();

        default DefaultOAuth2User build() {
            return new DefaultOAuth2User(this.providerId(), this.email(), this.name());
        }
    }

}

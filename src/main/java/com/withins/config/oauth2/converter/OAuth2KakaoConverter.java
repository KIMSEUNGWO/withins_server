package com.withins.config.oauth2.converter;

import org.springframework.security.oauth2.core.user.OAuth2User;

public final class OAuth2KakaoConverter implements OAuth2Converter {

    @Override
    public DefaultOAuth2User.Builder convert(OAuth2User user) {
        return new DefaultOAuth2User.Builder() {
            @Override
            public String providerId() {
                return user.getName();
            }

            @Override
            public String email() {
                return user.getAttribute("email");
            }

            @Override
            public String name() {
                return user.getAttribute("nickname");
            }
        };
    }
}

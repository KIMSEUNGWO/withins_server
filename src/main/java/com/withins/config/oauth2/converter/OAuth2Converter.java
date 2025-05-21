package com.withins.config.oauth2.converter;

import org.springframework.security.oauth2.core.user.OAuth2User;

public sealed interface OAuth2Converter permits
    OAuth2GoogleConverter,
    OAuth2KakaoConverter {

    DefaultOAuth2User.Builder convert(OAuth2User user);
}

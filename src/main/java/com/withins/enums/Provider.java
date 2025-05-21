package com.withins.enums;

import com.withins.config.oauth2.converter.DefaultOAuth2User;
import com.withins.config.oauth2.converter.OAuth2Converter;
import com.withins.config.oauth2.converter.OAuth2GoogleConverter;
import com.withins.config.oauth2.converter.OAuth2KakaoConverter;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@AllArgsConstructor
public enum Provider {

    GOOGLE(new OAuth2GoogleConverter()),
    KAKAO(new OAuth2KakaoConverter());

    private final OAuth2Converter converter;

    public static Provider findByProvider(String clientName) {
        return Provider.valueOf(clientName.toUpperCase());
    }

    public DefaultOAuth2User convert(OAuth2User user) {
        return converter.convert(user).build();
    }

}
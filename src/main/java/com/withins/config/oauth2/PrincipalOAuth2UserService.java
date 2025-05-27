package com.withins.config.oauth2;

import com.withins.config.oauth2.converter.DefaultOAuth2User;
import com.withins.entity.SocialLoginInfo;
import com.withins.entity.Member;
import com.withins.enums.Provider;
import com.withins.enums.Role;
import com.withins.jparepository.MemberJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class PrincipalOAuth2UserService extends DefaultOAuth2UserService {

    private final MemberJpaRepository repository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("PrincipalOAuth2UserService.loadUser");
        OAuth2User oAuth2User = super.loadUser(userRequest);
        System.out.println(oAuth2User.getAttributes());

        Provider provider = Provider.findByProvider(userRequest.getClientRegistration().getClientName());
        DefaultOAuth2User convert = provider.convert(oAuth2User);

        String username = provider + "_" + convert.getProviderId();

        Member member = repository.findByUsername(username)
            .orElseGet(() -> repository.save(Member.builder()
                .username(username)
                .nickname(provider.name() + "#" + new Random().ints(1, 10000).limit(1).findFirst().getAsInt())
                .role(Role.USER)
                .socialLoginInfo(new SocialLoginInfo(provider, convert.getProviderId()))
                .build())
            );
        return new PrincipalDetails(member);
    }
}

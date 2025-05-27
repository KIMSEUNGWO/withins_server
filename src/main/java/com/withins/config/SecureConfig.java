package com.withins.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withins.config.jwt.*;
import com.withins.config.jwt.formLogin.JsonAuthenticationFilter;
import com.withins.config.jwt.formLogin.OAuth2AuthenticationSuccessHandler;
import com.withins.config.oauth2.PrincipalDetailsService;
import com.withins.config.oauth2.PrincipalOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecureConfig {

    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;
    private final PrincipalDetailsService principalDetailsService;
    private final JwtTokenClearingLogoutHandler jwtTokenClearingLogoutHandler;
    private final JsonLogoutSuccessHandler jsonLogoutSuccessHandler;

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, PrincipalOAuth2UserService principalOAuth2UserService) throws Exception {

        AuthenticationManagerBuilder authManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authManagerBuilder.userDetailsService(principalDetailsService).passwordEncoder(bCryptPasswordEncoder());
        AuthenticationManager authenticationManager = authManagerBuilder.build();

        // FormLogin 유저 -> JWT 토큰 발급
        JsonAuthenticationFilter jsonAuthenticationFilter = new JsonAuthenticationFilter(objectMapper, jwtTokenProvider);
        jsonAuthenticationFilter.setAuthenticationManager(authenticationManager);

        http
            .addFilterAt(jsonAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
            .csrf(AbstractHttpConfigurer::disable)
//            .csrf(Customizer.withDefaults())
//            .csrf(csrf -> csrf
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//            )

            .authorizeHttpRequests(request -> request
                .requestMatchers("/api/user/**").authenticated()
                .requestMatchers("/auth/**").authenticated()
                .anyRequest().permitAll()
            )

            .cors(cors -> cors
                .configurationSource(devConfigurationSource())
            )

            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            )

            .formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/api/login")
                .defaultSuccessUrl("/")
            )

            .logout(logout -> logout
                .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.COOKIES)))
            )

            .oauth2Login(login -> login
                .loginPage("/oauth/login")
                .defaultSuccessUrl("/")
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(principalOAuth2UserService)
                )
                // OAuth2 유저 -> JWT 토큰 발급
                .successHandler(new OAuth2AuthenticationSuccessHandler(jwtTokenProvider))
                .failureHandler((request, response, exception) -> {
                    exception.printStackTrace();
                    response.sendRedirect("/login?error");
                })
            )

            .oauth2Client(Customizer.withDefaults())

            .oauth2ResourceServer(server -> server
                .bearerTokenResolver(new CookieBearerTokenResolver())
                .jwt(jwt -> jwt
                    .jwtAuthenticationConverter(new CustomJwtAuthenticationConverter())
                )
            )

            .logout(logout -> logout
                .logoutUrl("/api/logout")
                .deleteCookies("accessToken", "refreshToken", "JSESSIONID")
                .addLogoutHandler(jwtTokenClearingLogoutHandler)
                .logoutSuccessHandler(jsonLogoutSuccessHandler)
            )

            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )

            .authenticationManager(authenticationManager)
        ;
        return http.build();
    }

    UrlBasedCorsConfigurationSource devConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(List.of("http://localhost:[*]"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true); // 중요: credentials 허용
        configuration.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}

package com.withins.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.withins.config.oauth2.PrincipalDetails;
import com.withins.config.oauth2.PrincipalOAuth2UserService;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.HeaderWriterLogoutHandler;
import org.springframework.security.web.header.writers.ClearSiteDataHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SecureConfig {

    @Bean
    BCryptPasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http, PrincipalOAuth2UserService principalOAuth2UserService) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
//            .csrf(Customizer.withDefaults())
//            .csrf(csrf -> csrf
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//            )

            .authorizeHttpRequests(request ->
                request.anyRequest().permitAll()
            )

            .cors(cors -> cors
                .configurationSource(devConfigurationSource())
            )

            .sessionManagement(session -> session
                .invalidSessionUrl("/login?invalid")
                .maximumSessions(1)
                .maxSessionsPreventsLogin(true)
                .expiredUrl("/login?expired")
            )

            .formLogin(login -> login
                .loginPage("/login")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl("/")
                .successHandler((request, response, authentication) -> {

                    try {
                        response.setStatus(HttpServletResponse.SC_OK);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");

                        PrintWriter writer = response.getWriter();
                        writer.flush();
                        writer.close();
                    } catch (IOException e) {
                        System.out.println("e.getMessage() = " + e.getMessage());
                    }
                })
                .failureHandler((request, response, exception) -> {
                    System.out.println("exception = " + exception.getMessage());
                })
            )

            .oauth2Login(login -> login
                .loginPage("/oauth/login")
                .defaultSuccessUrl("/login/success")
                .userInfoEndpoint(userInfo -> userInfo
                    .userService(principalOAuth2UserService)
                )
                .failureHandler((request, response, exception) -> {
                    exception.printStackTrace();
                    response.sendRedirect("/login?error");
                })
            )

            .logout(logout -> logout
                .addLogoutHandler(new HeaderWriterLogoutHandler(new ClearSiteDataHeaderWriter(ClearSiteDataHeaderWriter.Directive.COOKIES)))
//                .deleteCookies("JSESSIONID", "remember-me")
            )
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

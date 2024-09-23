package com.example.backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/login", "/api/kakao/login").permitAll() // 로그인 관련 URL 허용
                        .anyRequest().permitAll()
                        //// 여기 수정하기..
                )
                .oauth2Login(oauth2 -> oauth2
                        .loginPage("/login") // 로그인 페이지 설정
                );

        return http.build();
    }
}

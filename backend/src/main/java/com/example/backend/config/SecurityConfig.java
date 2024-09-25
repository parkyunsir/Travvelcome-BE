package com.example.backend.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //.csrf(csrf -> csrf.disable()) // CSRF 비활성화 (필요한 경우)
//                .authorizeHttpRequests(auth -> auth
//                        .requestMatchers("/login", "/api/kakao/login", "/user").permitAll() // 로그인 관련 URL 허용
////                        .requestMatchers("/user").authenticated() // 인증된 사용자만 접근 가능
//                        .anyRequest().authenticated()
//                        //// 여기 수정하기..
//                )

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//                .csrf().disable()
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers("/", "/login").permitAll() // 이 경로들은 모두 접근 허용
                        .anyRequest().permitAll() // 추후 수정
                )
                .oauth2Login(oauth2 -> oauth2
                                .loginPage("/login")
//                        .userInfoEndpoint(userInfo -> userInfo
//                                .userService(customOAuth2UserService()) // OAuth2 로그인 후 사용자 서비스 설정
//                        )
                );

        return http.build();
    }

    // CustomOAuth2UserService를 빈으로 등록
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> customOAuth2UserService() {
//        return new CustomOAuth2UserService();
//    }
}

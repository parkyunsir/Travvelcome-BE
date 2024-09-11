//package com.example.backend.config;
//
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//@Configuration
//@EnableWebSecurity
//public class SecurityConfig {
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(csrf -> csrf.disable()) // CSRF 보호 비활성화
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers("/login/**", "/callback").permitAll() // 로그인 및 콜백 경로에 대한 접근 허용
//                        .anyRequest().authenticated() // 그 외의 모든 요청은 인증 필요
//                )
//                .formLogin(form -> form
//                        .loginPage("/login/page") // 커스텀 로그인 페이지 설정
//                        .permitAll()
//                )
//                .logout(logout -> logout
//                        .permitAll()
//                );
//
//        return http.build();
//    }
//}

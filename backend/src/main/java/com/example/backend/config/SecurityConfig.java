package com.example.backend.config;

//import com.example.backend.config.oauth.CustomOAuth2UserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;


@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf().disable() // test 과정에서만 비활성화
            .authorizeHttpRequests(authorize -> authorize
                .requestMatchers("/", "/login", "/callback/**").permitAll()  // requestMatchers로 변경
              .anyRequest().permitAll() // 일단 이렇게..
//                .anyRequest().authenticated() //
            )
            .oauth2Login(oauth2 -> oauth2
                .defaultSuccessUrl("/main", true)  // 로그인 성공 후 리다이렉트할 URL
            );
        return http.build();
    }

    // 사용자 정보를 받아오는 Kakao OAuth2UserService 설정
//    @Bean
//    public OAuth2UserService<OAuth2UserRequest, OAuth2User> kakaoOAuth2UserService() {
//        return userRequest -> {
//            OAuth2User oAuth2User = new CustomOAuth2UserService().loadUser(userRequest);
//            return oAuth2User;
//        };
//    }
}
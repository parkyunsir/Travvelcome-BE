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
}

//package com.example.backend.config;
//
////import com.example.backend.config.oauth.CustomOAuth2UserService;
//import com.example.backend.config.oauth.CustomOAuth2UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.security.config.annotation.web.builders.HttpSecurity;
//import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
//import org.springframework.security.web.SecurityFilterChain;
//
//
//    @Configuration
//    @EnableWebSecurity
//    public class SecurityConfig {
//
//        @Autowired
//        private CustomOAuth2UserService customOAuth2UserService;
//
//        @Bean
//        public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//            http
//                    .csrf().disable() // test 과정에서만 비활성화
//                    .authorizeHttpRequests(authorize -> authorize
//                                    .requestMatchers("/", "/login", "/callback/**").permitAll()  // requestMatchers로 변경
//                                    .requestMatchers("/chat/**").authenticated()  // requestMatchers로 변경
//                                    .anyRequest().permitAll() // 일단 이렇게..
////                .anyRequest().authenticated() //
//                    )
//                    .oauth2Login(oauth2 -> oauth2
//                            .userInfoEndpoint(userInfo -> userInfo
//                                    .userService(customOAuth2UserService)  // OAuth2UserService 등록
//                            )
//                            .defaultSuccessUrl("/main", true)  // 로그인 성공 후 리다이렉트할 URL 설정
//                    );
//            // 로그인 성공 후 리다이렉트할 URL 설정
//
//            return http.build();
//        }
//    }
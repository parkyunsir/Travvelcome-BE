//package com.example.backend.config.oauth;
//
//import com.example.backend.model.UsersEntity;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Map;
//
//public class CustomUserDetails implements OAuth2User {
//
//    private UsersEntity user;
//
//    public CustomUserDetails(UsersEntity user) {
//        this.user = user;
//    }
//
//    @Override
//    public Map<String, Object> getAttributes() {
//        return Map.of(
//                "id", user.getId(),
//                "email", user.getEmail(),
//                "nickname", user.getNickname()
//        );
//    }
//
//    @Override
//    public Collection<? extends GrantedAuthority> getAuthorities() {
//        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_USER"));
//    }
//
//    @Override
//    public String getName() {
//        return user.getNickname();  // 사용자 닉네임 반환
//    }
//}

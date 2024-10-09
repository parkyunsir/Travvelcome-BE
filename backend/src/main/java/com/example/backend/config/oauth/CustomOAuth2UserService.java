//package com.example.backend.config.oauth;
//
//import com.example.backend.model.UsersEntity;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//
//@Service
//public class CustomOAuth2UserService extends DefaultOAuth2UserService {
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        // DefaultOAuth2UserService에서 사용자 정보를 가져옴
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//
//        // 사용자 정보를 원하는 방식으로 매핑
//        UsersEntity user = mapToUsersEntity(oAuth2User);
//
//        // 커스텀 UserDetails 반환
//        return new CustomUserDetails(user);
//    }
//
//    private UsersEntity mapToUsersEntity(OAuth2User oAuth2User) {
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
//        Map<String, Object> profile = (Map<String, Object>) kakaoAccount.get("profile");
//
//
//        Long id = ((Number) attributes.get("id")).longValue();
//        String email = (String) kakaoAccount.get("email");
//        String nickname = (String) profile.get("nickname");
//
//        return new UsersEntity(
//                id,
//                email,
//                nickname
//        );
//    }
//
//}
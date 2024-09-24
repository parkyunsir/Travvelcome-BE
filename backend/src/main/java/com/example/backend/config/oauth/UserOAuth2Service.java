//package com.example.backend.config.oauth;
//
//import com.example.backend.model.UsersEntity;
//import com.example.backend.repository.UsersRepository;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
//import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
//import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.stereotype.Service;
//
//import java.util.Map;
//
//@Slf4j
//@RequiredArgsConstructor
//@Service
//public class UserOAuth2Service extends DefaultOAuth2UserService {
//    @Autowired
//    private UsersRepository usersRepository;
//    private UsersEntity userInfo;
//
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//        Map<String, Object> attributes = oAuth2User.getAttributes();
//
////        Map<String, Object> kakao_account = (Map<String, Object>) attributes.get("kakao_account");
////        String email = (String) kakao_account.get("email");
////        log.info("email is : "+email);
////
////        if (email == null) {
////            log.info("email is null");
////            email = "kakao_email";
////        }
//
//        Map<String, Object> properties = (Map<String, Object>) attributes.get("properties");
//        String nickname = (String) properties.get("nickname");
//        log.info("nickname is : "+nickname);
//
////        UsersEntity user = usersRepository.findOptionalByEmail(email)
//        UsersEntity user = usersRepository.findOptionalByNickname(nickname)
//                .map(entity -> entity.update(nickname))
//                .orElse(UsersEntity.builder()
////                        .email(email)
//                        .nickname(nickname)
//                        .build());
//        userInfo = usersRepository.save(user);
//
//        return oAuth2User;
//    }
//
//    public String getUserNickname() {
//        if (userInfo.getNickname() != null ){
//            return userInfo.getNickname();
//        } else {
//            return "null";
//
//        }
//    }
//}
//

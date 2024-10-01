package com.example.backend.config.oauth;

import com.example.backend.model.UsersEntity;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    @Autowired
    private UserRepository userRepository; // UserRepository를 주입

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) {
        // 카카오로부터 사용자 정보를 받아옴
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 카카오의 사용자 고유 id (Long 타입)와 닉네임을 추출
        Long id = Long.valueOf(attributes.get("id").toString());  // id를 Long 타입으로 변환
        String nickname = (String) ((Map<String, Object>) attributes.get("properties")).get("nickname");

        // 사용자 정보를 저장하는 로직 (id 기반으로 저장)
        saveUser(id, nickname);

        // DefaultOAuth2User로 반환하여 권한 및 사용자 정보 제공
        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")), // 기본 사용자 권한 부여
                attributes, // 카카오로부터 받은 사용자 정보
                "id"  // 사용자 고유 식별자로 사용할 필드
        );
    }

    private void saveUser(Long id, String nickname) {
        UsersEntity user = new UsersEntity(id, nickname); // id로 저장
        userRepository.save(user);
    }
}

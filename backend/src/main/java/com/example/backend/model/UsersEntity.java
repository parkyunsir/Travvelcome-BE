package com.example.backend.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;
//import java.util.HashMap;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="Users")
public class UsersEntity {

    @Id
    private Long id; // 카카오 id를 저장한다.
    private String email;
    private String nickname;
    private String thumbnailImageUrl;
    private String profileImageUrl;
    private String provider;
    private String providerId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Interest> interests;
//
//    private static UsersEntity ofKakao(Map<String, Object> attributes) {
//        return UsersEntity.builder()
//                .provider("kakao")
//                .id(Long.valueOf("kakao_" + attributes.get("id")))
//                .nickname((String) ((Map) attributes.get("properties")).get("nickname"))
//                .thumbnailImageUrl((String) ((Map) attributes.get("properties")).get("thumbnail_image_url"))
//                .profileImageUrl((String) ((Map) attributes.get("properties")).get("profile_image_url"))
//                .build();
//    }
//
////
//
//

}
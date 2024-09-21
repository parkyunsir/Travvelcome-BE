package com.example.backend.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashMap;

@Getter
@NoArgsConstructor //역직렬화를 위한 기본 생성자
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserDto {

    //회원 번호
    @JsonProperty("id")
    public Long id;

    //사용자 프로필 정보
    @JsonProperty("profile")
    public Profile profile;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Profile {

        //닉네임
        @JsonProperty("nickname")
        public String nickName;

        //프로필 미리보기 이미지 URL
        @JsonProperty("thumbnail_image_url")
        public String thumbnailImageUrl;

        //프로필 사진 URL
        @JsonProperty("profile_image_url")
        public String profileImageUrl;
    }
}
package com.example.backend.dto;

import com.example.backend.model.UsersEntity;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@NoArgsConstructor //역직렬화를 위한 기본 생성자
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUserDto {

    //회원 번호
    @JsonProperty("id")
    public Long id;

    @JsonProperty("kakao_account")
    public KakaoAccount kakaoAccount;

    @Getter
    @NoArgsConstructor
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class KakaoAccount { //profile, name, email

        //사용자 프로필 정보
        @JsonProperty("profile")
        public Profile profile;

        //카카오계정 대표 이메일
        @JsonProperty("email")
        public String email;

        //연령대
//        @JsonProperty("age_range")
//        public String ageRange;


        //출생 연도 (YYYY 형식)
//        @JsonProperty("birthyear")
//        public String birthYear;

        //생일 (MMDD 형식)
//        @JsonProperty("birthday")
//        public String birthDay;

        //성별
//        @JsonProperty("gender")
//        public String gender;

        //전화번호 +82 00-0000-0000 형식
//        @JsonProperty("phone_number")
//        public String phoneNumber;

        @Getter
        @NoArgsConstructor
        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Profile { // nickname, thumbnailImageUrl, profileImageUrl

            //닉네임
            @JsonProperty("nickname")
            public String nickName;

            //프로필 미리보기 이미지 URL
            @JsonProperty("thumbnail_image_url")
            public String thumbnailImageUrl;

            //프로필 사진 URL
            @JsonProperty("profile_image_url")
            public String profileImageUrl;

            public Profile(UsersEntity entity){
                this.nickName = entity.getNickname();
                this.thumbnailImageUrl = entity.getThumbnailImageUrl();
                this.profileImageUrl = entity.getProfileImageUrl();
        }
    }

    public KakaoAccount(UsersEntity entity){
        this.email = entity.getEmail();
        this.profile = new Profile(entity);
    }

    }

    public KakaoUserDto(UsersEntity entity){
        this.id = entity.getId();
        this.kakaoAccount = new KakaoAccount(entity);
    }

    public static UsersEntity toEntity(final KakaoUserDto dto){
        return UsersEntity.builder()
                .id(dto.getId())
                .email(dto.getKakaoAccount().getEmail())
                .nickname(dto.getKakaoAccount().getProfile().getNickName())
                .thumbnailImageUrl(dto.getKakaoAccount().getProfile().getThumbnailImageUrl())
                .profileImageUrl(dto.getKakaoAccount().getProfile().getProfileImageUrl())
                .build();
    }


    public static UsersEntity toEntity(final KakaoAccount dto) {
        return UsersEntity.builder()
                .email(dto.getEmail())
                .nickname(dto.getProfile().getNickName())
                .thumbnailImageUrl(dto.getProfile().getThumbnailImageUrl())
                .profileImageUrl(dto.getProfile().getProfileImageUrl())
                .build();
    }

    public static UsersEntity toEntity(final KakaoAccount.Profile dto){
        return UsersEntity.builder()
                .nickname(dto.getNickName())
                .thumbnailImageUrl(dto.getThumbnailImageUrl())
                .profileImageUrl(dto.getProfileImageUrl())
                .build();
    }
}
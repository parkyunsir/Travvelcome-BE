package com.example.backend.dto;

import com.example.backend.model.UsersEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class UsersDTO {

    private Long id;
    public Long kakaoId; //카카오에서 받은 id
    private String nickname;
    private String thumbnailImageUrl;
    private String profileImageUrl;

    public UsersDTO(UsersEntity entity) {
        this.id = entity.getId();
        this.kakaoId = entity.getKakaoId();
        this.nickname = entity.getNickname();
        this.profileImageUrl = entity.getProfileImageUrl();
        this.thumbnailImageUrl = entity.getThumbnailImageUrl();
    }

    public static UsersEntity toEntity(final UsersDTO dto) {
        return UsersEntity.builder()
                .id(dto.getId())
                .kakaoId(dto.getKakaoId())
                .nickname(dto.getNickname())
                .profileImageUrl(dto.getProfileImageUrl())
                .thumbnailImageUrl(dto.thumbnailImageUrl)
                .build();
    }
}

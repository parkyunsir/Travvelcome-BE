package com.example.backend.dto;

import com.example.backend.model.Interest;
import com.example.backend.model.UsersEntity;
import com.example.backend.model.enums.Category;
import com.example.backend.model.enums.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InterestDTO {
    private Long id;
    private UsersEntity user;
//    private Long userId;
    private Category category;
    private Tag tag;

    public InterestDTO(Interest entity) {
        this.id = entity.getId();
        this.user = entity.getUser();
//        this.userId = entity.getUserId();
        this.category = entity.getCategory();
        this.tag = entity.getTag();
    }

    public Interest toEntity(final InterestDTO dto) {
        return Interest.builder()
                .id(dto.getId())
//                .userId(dto.getUserId())
                .user(dto.getUser())
                .category(dto.getCategory())
                .tag(dto.getTag())
                .build();
    }
}

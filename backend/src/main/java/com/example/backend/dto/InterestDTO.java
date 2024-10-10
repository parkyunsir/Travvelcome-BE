package com.example.backend.dto;

import com.example.backend.model.Interest;
import com.example.backend.model.enums.Category;
import com.example.backend.model.enums.Tag;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Getter
@Setter
@JsonIgnoreProperties({"id", "tag"})
public class InterestDTO {
    private Long id;
    private Category category;
    private Tag tag;

    public InterestDTO(Interest entity) {
        this.id = entity.getId();
        this.category = entity.getCategory();
        this.tag = entity.getTag();
    }

    // Category를 받는 생성자 추가
    public InterestDTO(Category category) {
        this.category = category;
    }

    public Interest toEntity(final InterestDTO dto) {
        return Interest.builder()
                .id(dto.getId())
                .category(dto.getCategory())
                .tag(dto.getTag())
                .build();
    }
}

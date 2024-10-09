package com.example.backend.dto;

import com.example.backend.model.Interest;
import com.example.backend.model.UsersEntity;
import com.example.backend.model.enums.Category;
import com.example.backend.model.enums.Tag;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class InterestDTO {
    private Long id;
    private Category category;
    private List<Category> categories;
    private Tag tag;

    public InterestDTO(Interest entity) {
        this.id = entity.getId();
        this.category = entity.getCategory();
        this.categories = entity.getCategories();
        this.tag = entity.getTag();
    }

    public InterestDTO(List<Interest> entity) {

    }

    public Interest toEntity(final InterestDTO dto) {
        return Interest.builder()
                .id(dto.getId())
                .category(dto.getCategory())
                .categories(dto.getCategories())
                .tag(dto.getTag())
                .build();
    }
}

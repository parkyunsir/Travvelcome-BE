//package com.example.backend.dto;
//
//import com.example.backend.model.ChatEntity;
//import com.example.backend.model.Interest;
//import com.example.backend.model.UsersEntity;
//import com.example.backend.model.enums.Category;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Builder
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//public class InterestDTO {
//    private Long id;
//    private UsersEntity user;
//    private Category category;
//
//    public InterestDTO(Interest entity) {
//        this.id = entity.getId();
//        this.user = entity.getUser();
//        this.category = entity.getCategory();
//    }
//
//    public Interest toEntity(final InterestDTO dto) {
//        return Interest.builder()
//                .id(dto.getId())
//                .user(dto.getUser())
//                .category(dto.getCategory())
//                .build();
//    }
//}

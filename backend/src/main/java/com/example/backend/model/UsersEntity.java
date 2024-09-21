package com.example.backend.model;

import com.example.backend.model.enums.Category;
import jakarta.persistence.*;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
//import java.util.HashMap;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name="Users")
public class UsersEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long kakaoId; // 카카오 고유 ID
    private String nickname;
    private String thumbnailImageUrl;
    private String profileImageUrl;

    @ElementCollection(targetClass = Category.class)
    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private List<Category> categories;
}
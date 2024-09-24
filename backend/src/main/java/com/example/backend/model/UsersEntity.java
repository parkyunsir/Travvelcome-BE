package com.example.backend.model;

import jakarta.persistence.*;
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
    private Long id; // 카카오 id를 저장한다.
    private String email;
    private String nickname;
    private String thumbnailImageUrl;
    private String profileImageUrl;
}
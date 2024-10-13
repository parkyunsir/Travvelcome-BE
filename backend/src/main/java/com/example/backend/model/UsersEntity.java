package com.example.backend.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.Map;

import lombok.*;
import org.springframework.security.core.userdetails.User;
//import java.util.HashMap;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Getter
@Setter
@Table(name="Users")
public class UsersEntity {

    @Id
    private Long id; // 카카오 id를 저장한다.
    private String email;
    private String nickname;
    private String thumbnailImageUrl;
    private String profileImageUrl;

//    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<Interest> interests;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Interest> interests;

    public UsersEntity(Long id, String nickname) {
        this.id = id;
        this.nickname = nickname;
    }

    public UsersEntity(Long id, String email, String nickname) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
    }
}
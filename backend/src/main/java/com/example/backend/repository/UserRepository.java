package com.example.backend.repository;

import com.example.backend.model.Interest;
import com.example.backend.model.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRepository extends JpaRepository<UsersEntity, Long> {

//    Optional<UsersEntity> findOptionalByNickname(String nickname);
//    Optional<UsersEntity> findOptionalByEmail(String email);

    // UsersEntity의 id로 Interest 목록 조회
    @Query("SELECT u.interests FROM UsersEntity u WHERE u.id = :userId")
    List<Interest> findInterestsByUserId(Long userId);
}

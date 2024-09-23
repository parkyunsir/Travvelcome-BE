package com.example.backend.repository;

import com.example.backend.model.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

//    UsersEntity findByEmail(String email);  // email을 이용해서 회원 정보 찾음

    Optional<UsersEntity> findById(Long id);
    Optional<UsersEntity> findByNickname(String nickname);
}

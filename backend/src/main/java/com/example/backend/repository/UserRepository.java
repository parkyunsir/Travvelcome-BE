package com.example.backend.repository;

import com.example.backend.model.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UsersEntity, Long> {

//    Optional<UsersEntity> findOptionalByNickname(String nickname);
//    Optional<UsersEntity> findOptionalByEmail(String email);
}

package com.example.backend.repository;

import com.example.backend.model.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

//    Optional<UsersEntity> findOptionalByNickname(String nickname);
//    Optional<UsersEntity> findOptionalByEmail(String email);
}

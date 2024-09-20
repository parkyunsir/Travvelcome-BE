package com.example.backend.repository;

import com.example.backend.model.UsersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<UsersEntity, Long> {

    UsersEntity findByKakaoId(Long kakaoId);

}

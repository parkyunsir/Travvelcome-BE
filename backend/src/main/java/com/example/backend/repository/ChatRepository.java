package com.example.backend.repository;

import com.example.backend.model.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, String> {

    // void deleteByUserId(String userId);

    // landmark별로 chat 내역 출력
    List<ChatEntity> findByLandmarkId(Long landmarkId);
}

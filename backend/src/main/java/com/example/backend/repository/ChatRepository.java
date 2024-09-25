package com.example.backend.repository;

import com.example.backend.model.ChatEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatRepository extends JpaRepository<ChatEntity, String> {

    // landmark별로 chat 내역 출력
    List<ChatEntity> findByLandmarkId(Long landmarkId);

    // landmark별 date chatList 가져오기
    @Query("SELECT c FROM ChatEntity c WHERE c.landmarkId IN :landmarkIds " +
            "AND c.date = (SELECT MAX(c2.date) FROM ChatEntity c2 WHERE c2.landmarkId = c.landmarkId) " +
            "ORDER BY c.date DESC")
    List<ChatEntity> findLatestChatByLandmarkIds(@Param("landmarkIds") List<Long> landmarkIds);


    // 대화 내역 검색하기
    List<ChatEntity> findBySentContainingOrReceivedContaining(String sent, String received);
}
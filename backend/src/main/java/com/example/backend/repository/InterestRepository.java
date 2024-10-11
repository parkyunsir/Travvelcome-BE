package com.example.backend.repository;

import com.example.backend.model.Interest;
import com.example.backend.model.enums.Category;
import com.example.backend.model.enums.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface InterestRepository  extends JpaRepository<Interest, Long> {

    void deleteByUserId(long userId);

    // 관심사 삭제하기
    @Transactional
    @Modifying
    @Query("DELETE FROM Interest i WHERE i.user.id = :userId")
    void deleteInterestsByUserId(Long userId);
}

package com.example.backend.repository;

import com.example.backend.model.Landmark;
import com.example.backend.model.enums.Category;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface LandmarkRepository extends JpaRepository<Landmark, Long> {

  List<Landmark> findByTitleContainingOrDescriptionContaining(String keyword, String keyword1);

  List<Landmark> findByCategoriesIn(List<Category> interests);

  List<Landmark> findByCategoriesContaining(Category category);

  // id를 통해 title 추출
  @Query("SELECT l.title FROM Landmark l WHERE l.id = :id")
  Optional<String> findTitleById(@Param("id") Long id);

  
  // id와 title로 가져오기
  List<Landmark> findByIdInAndTitleContaining(List<Long> landmarkIds, String title);

  // 특정 카테고리 목록에 포함된 랜드마크 찾기
  @Query("SELECT l FROM Landmark l JOIN l.categories c WHERE c IN :categories")
  List<Landmark> findByCategories(@Param("categories") List<Category> categories);

}
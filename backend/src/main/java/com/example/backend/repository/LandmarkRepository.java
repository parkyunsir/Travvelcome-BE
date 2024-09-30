package com.example.backend.repository;

import com.example.backend.model.Landmark;
import com.example.backend.model.enums.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LandmarkRepository extends JpaRepository<Landmark, Long> {

  List<Landmark> findByTitleContainingOrDescriptionContaining(String keyword, String keyword1);

  List<Landmark> findByCategoriesIn(List<Category> interests);

  List<Landmark> findByCategoriesContaining(Category category);

  // title에 포함된 모든 Landmark 엔티티를 검색
  List<Landmark> findByTitleContaining(String title);

  // 모든 id를 통해 Landmark Entity 반환
  List<Landmark> findAllById(Long id);
}
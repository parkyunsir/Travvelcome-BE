package com.example.backend.repository;

import com.example.backend.dto.LandmarkResponseDTO.LandmarkPreViewDTO;
import com.example.backend.model.Landmark;
import com.example.backend.model.enums.Category;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LandmarkRepository extends JpaRepository<Landmark, Long> {

  List<Landmark> findByTitleContainingOrDescriptionContaining(String keyword, String keyword1);

  List<Landmark> findByCategoriesIn(List<Category> interests);

  List<Landmark> findByCategoriesContaining(Category category);
}
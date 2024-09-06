package com.example.backend.service;

import com.example.backend.dto.LandmarkResponseDTO.LandmarkPreViewDTO;
import com.example.backend.model.enums.Category;
import java.util.List;

public interface LandmarkService {

  void fetchAndSaveLandmarks();

  List<LandmarkPreViewDTO> searchLandmark(String keyword);

  List<LandmarkPreViewDTO> getLandmarks(String category, List<Category> interests);
}

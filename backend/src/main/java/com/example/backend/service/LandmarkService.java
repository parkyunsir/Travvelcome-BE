package com.example.backend.service;

import com.example.backend.dto.LandmarkResponseDTO.LandmarkFindDTO;
import com.example.backend.dto.LandmarkResponseDTO.LandmarkMapDTO;
import com.example.backend.dto.LandmarkResponseDTO.LandmarkPreViewDTO;
import com.example.backend.model.enums.Category;
import java.util.List;

public interface LandmarkService {

  void fetchAndSaveLandmarks();

  List<LandmarkPreViewDTO> searchLandmark(String keyword);

  List<LandmarkPreViewDTO> getLandmarks(String category, List<Category> interests);

  List<LandmarkPreViewDTO> categoryLandmark(Category category);

  LandmarkFindDTO getLandmarkFind(Long landmarkId);

  List<LandmarkPreViewDTO> getCloseLandmarks(double mapX, double mapY);

  List<LandmarkMapDTO> getMapLandmarks(long userId);

  void findLandmark(Long landmarkId, long userId);
}

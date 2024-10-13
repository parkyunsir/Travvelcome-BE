package com.example.backend.service;

import com.example.backend.dto.LandmarkResponseDTO.LandmarkFindDTO;
import com.example.backend.dto.LandmarkResponseDTO.LandmarkMapDTO;
import com.example.backend.dto.LandmarkResponseDTO.LandmarkPreViewDTO;
import com.example.backend.dto.LandmarkResponseDTO.LandmarkRecommendDTO;
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

  List<LandmarkRecommendDTO> getRecommendedLandmarks(double mapX, double mapY, long userId);

  // 나의 관심사와 landmark의 관심사가 일치하면 category 출력.
  List<String> findCategories(List<String> landmarkCategories, List<String> categoryList);
}

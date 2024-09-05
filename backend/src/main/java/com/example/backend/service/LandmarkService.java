package com.example.backend.service;

import com.example.backend.dto.LandmarkResponseDTO.LandmarkPreViewDTO;
import java.util.List;

public interface LandmarkService {

  void fetchAndSaveLandmarks();

  List<LandmarkPreViewDTO> searchLandmark(String keyword);
}

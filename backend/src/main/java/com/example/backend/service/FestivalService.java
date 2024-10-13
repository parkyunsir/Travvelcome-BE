package com.example.backend.service;

import com.example.backend.dto.FestivalResponseDTO.FestivalDTO;
import java.util.List;

public interface FestivalService {

  List<FestivalDTO> getFestivals();

  void fetchAndSaveFestivals();
}

package com.example.backend.controller;

import com.example.backend.apiPayload.ApiResponse;
import com.example.backend.dto.LandmarkResponseDTO.LandmarkPreViewDTO;
import com.example.backend.model.enums.Category;
import com.example.backend.service.LandmarkService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/landmarks")
public class LandmarkController {

  private final LandmarkService landmarkService;

  @GetMapping("/fetch-landmarks")
  public String fetchLandmarks() {
    landmarkService.fetchAndSaveLandmarks();
    return "Landmarks have been fetched and saved successfully!";
  }

  @Operation(summary = "랜드마크 검색 API", description = "랜드마크 리스트 페이지에서의 검색 API입니다. 검색어 RequestParam 입니다!")
  @GetMapping("/search")
  public ApiResponse<List<LandmarkPreViewDTO>> searchLandmark(@RequestParam("keyword") String keyword) {
    List<LandmarkPreViewDTO> result = landmarkService.searchLandmark(keyword);
    return ApiResponse.onSuccess(result);
  }

  @Operation(summary = "랜드마크 목록 조회 API", description = "랜드마크 리스트 페이지에서의 목록 조회 API입니다. category(nature, history, culture)")
  @GetMapping("")
  public List<LandmarkPreViewDTO> getLandmarks(@RequestParam(name = "category", required = false) String category,
      @RequestParam(name = "interest", required = false) List<Category> interests) {
    return landmarkService.getLandmarks(category, interests);
  }

}

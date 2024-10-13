package com.example.backend.controller;

import com.example.backend.apiPayload.ApiResponse;
import com.example.backend.dto.FestivalResponseDTO.FestivalDTO;
import com.example.backend.dto.LandmarkResponseDTO.LandmarkPreViewDTO;
import com.example.backend.service.FestivalService;
import com.example.backend.service.LandmarkService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/festivals")
public class FestivalController {

  private final FestivalService festivalService;

  @GetMapping("/fetch-festivals")
  public String fetchAndSaveFestivals() {
    festivalService.fetchAndSaveFestivals();
    return "Landmarks have been fetched and saved successfully!";
  }

  @Operation(summary = "행사 정보 조회 API", description = "행사 정보 조회 API입니다.")
  @GetMapping("")
  public ApiResponse<List<FestivalDTO>> getFestivals() {
    List<FestivalDTO> result = festivalService.getFestivals();
    return ApiResponse.onSuccess(result);
  }

}

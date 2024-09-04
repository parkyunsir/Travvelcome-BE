package com.example.backend.controller;

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

}

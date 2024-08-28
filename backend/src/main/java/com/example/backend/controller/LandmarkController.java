package com.example.backend.controller;

import com.example.backend.service.LandmarkService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LandmarkController {

  private final LandmarkService landmarkService;

  @GetMapping("/fetch-landmarks")
  public String fetchLandmarks() {
    landmarkService.fetchAndSaveLandmarks();
    return "Landmarks have been fetched and saved successfully!";
  }

}

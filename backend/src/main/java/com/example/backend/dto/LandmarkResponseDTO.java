package com.example.backend.dto;

import com.example.backend.model.enums.Category;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class LandmarkResponseDTO {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class LandmarkPreViewDTO {
    Long landmarkId;
    String title;
    String description;
    List<Category> categories;
    String imageUrl;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class LandmarkMapDTO {
    Long landmarkId;
    private double mapX;
    private double mapY;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class LandmarkFindDTO {
    Long landmarkId;
    String title;
    String description;
    List<Category> categories;
    String imageUrl;
    String addr1;
    String addr2;
  }

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class LandmarkRecommendDTO {
    Long landmarkId;
    String title;
    List<Category> categories;
    String imageUrl;
    double distance;
  }

}

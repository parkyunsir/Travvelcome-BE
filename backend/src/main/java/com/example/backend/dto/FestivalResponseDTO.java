package com.example.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class FestivalResponseDTO {

  @Builder
  @Getter
  @NoArgsConstructor
  @AllArgsConstructor
  public static class FestivalDTO {
    Long id;
    String title;                // 제목
    String addr1;                // 주소
    String addr2;                // 상세주소
    Long contentId;              // 콘텐츠 ID
    String createdTime;           // 등록일
    String eventStartDate;       // 행사 시작일 (형식: YYYYMMDD)
    String eventEndDate;         // 행사 종료일 (형식: YYYYMMDD)
    String firstImage;           // 대표 이미지 원본 URL
    String firstImage2;          // 대표 이미지 썸네일 URL
    Double mapX;                 // GPS X 좌표
    Double mapY;                 // GPS Y 좌표
    String modifiedTime;         // 수정일
    String tel;                  // 전화번호
  }

}

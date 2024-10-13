package com.example.backend.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Festival {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String title;                // 제목
  private String addr1;                // 주소
  private String addr2;                // 상세주소
  private String booktour;             // 교과서속여행지 여부
  private String cat1;                 // 대분류 코드
  private String cat2;                 // 중분류 코드
  private String cat3;                 // 소분류 코드
  private Long contentId;              // 콘텐츠 ID
  private Long contentTypeId;          // 콘텐츠 타입 ID
  private String createdTime;           // 등록일
  private String eventStartDate;       // 행사 시작일 (형식: YYYYMMDD)
  private String eventEndDate;         // 행사 종료일 (형식: YYYYMMDD)
  private String firstImage;           // 대표 이미지 원본 URL
  private String firstImage2;          // 대표 이미지 썸네일 URL
  private String cpyrhtDivCd;          // 저작권 유형
  private Double mapX;                 // GPS X 좌표
  private Double mapY;                 // GPS Y 좌표
  private Integer mlevel;              // 맵 레벨
  private String modifiedTime;         // 수정일
  private Integer areaCode;            // 지역 코드
  private Integer sigunguCode;         // 시군구 코드
  private String tel;                  // 전화번호

}

package com.example.backend.controller;

import com.example.backend.apiPayload.ApiResponse;
import com.example.backend.apiPayload.code.status.SuccessStatus;
import com.example.backend.dto.KakaoDto;
import com.example.backend.model.enums.Category;
import com.example.backend.service.KakaoService;
import com.example.backend.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {

  private final MyPageService myPageService;
  private final KakaoService kakaoService;

  @Operation(summary = "스탬프(프리퀀시) 개수 조회 API", description = "스탬프(프리퀀시)의 개수를 조회하는 기능입니다. userId에 토큰을 입력해주세요.")
  @GetMapping("/stamp")
  public ApiResponse<Long> getUserStampCount(@RequestParam String userId) {
    KakaoDto kakaoDto = kakaoService.getUserInfo(userId);
    Long stampCount = myPageService.getUserStampCount(kakaoDto.getId());
    return ApiResponse.onSuccess(stampCount);
  }

  @Operation(summary = "유저 관심사 수정 API", description = "유저의 관심사를 수정하는 기능입니다. userId에 토큰을 입력해주세요.")
  @PutMapping("/interest")
  public ApiResponse updateUserInterest(@RequestParam(name = "categories", required = false) List<Category> categories, @RequestParam String userId) {
    KakaoDto kakaoDto = kakaoService.getUserInfo(userId);
    myPageService.updateUserInterest(categories ,kakaoDto.getId());
    return ApiResponse.onSuccess(SuccessStatus._OK);
  }

}

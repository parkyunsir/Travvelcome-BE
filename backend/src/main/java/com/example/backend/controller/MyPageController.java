package com.example.backend.controller;

import com.example.backend.apiPayload.ApiResponse;
import com.example.backend.service.MyPageService;
import io.swagger.v3.oas.annotations.Operation;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/mypage")
public class MyPageController {

  private final MyPageService myPageService;

  @Operation(summary = "스탬프(프리퀀시) 개수 조회 API", description = "스탬프(프리퀀시)의 개수를 조회하는 기능입니다.")
  @GetMapping("/stamp")
  public ApiResponse<Long> getUserStampCount(@RequestParam String userId) {
    Long stampCount = myPageService.getUserStampCount(Long.parseLong(userId));
    return ApiResponse.onSuccess(stampCount);
  }

}

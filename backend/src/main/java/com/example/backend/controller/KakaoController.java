package com.example.backend.controller;

import com.example.backend.dto.KakaoDto;
import com.example.backend.model.UsersEntity;
import com.example.backend.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;


// 추후에 login.html 파일 지우고 리액트와 연결시키기

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoController {

    @Autowired
    private KakaoService kakaoService;

    @Operation(summary = "로그인 API", description = "로그인 API입니다. code값과 token값이 자동 생성됩니다!")
    @GetMapping("/callback") // 사용자 정보
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        try {
            String accessToken = kakaoService.getAccessTokenFromKakao(code);

            KakaoDto userInfo = kakaoService.getUserInfo(accessToken);
            UsersEntity getUserEntity = KakaoDto.toEntity(userInfo);

            UsersEntity savedEntity = kakaoService.saveUser(getUserEntity);

            // 토큰과 사용자 정보를 Map으로 묶어서 반환
            Map<String, Object> responseBody = new HashMap<>();
            responseBody.put("accessToken", accessToken); // 토큰 추가
            responseBody.put("userInfo", savedEntity); // 사용자 정보 추가

            return ResponseEntity.ok().body(responseBody);
        } catch (Exception e) {
            // 6. 예외 발생 시 처리
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("서버 에러, 관리자에게 문의 바랍니다.");
        }
    }

    // 로그아웃
    @Operation(summary = "로그아웃 API", description = "로그아웃 API입니다. RequestPram userId에 토큰을 입력해주세요." +
            " 현재 swagger에서 동작 시 200 ok & html 문서가 뜹니다.")
    @PostMapping("/logout")
    public ResponseEntity<?> kakaoLogout(@RequestParam String userId) {

        if (userId != null && !userId.isEmpty()) {
            try {
                String message = kakaoService.logout(userId); // 로그아웃 처리

                return ResponseEntity.ok().body(message); // 성공 시 Id 반환
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage()); // 예외 발생 시 에러 메시지 반환
            }
        } else {
            return ResponseEntity.badRequest().body("accessToken is null or empty"); // accessToken이 없는 경우 처리
        }
    }

    // 계정 탈퇴
    @Operation(summary = "계정탈퇴 API", description = "계정 탈퇴(실제로는 카카오 계정과 앱과 연결을 끊는) API입니다. RequestPram userId에 토큰을 입력해주세요." +
            " 현재 swagger에서 동작 시 Invalid Parameter: 4xx error가 뜨지만 정상 처리 된 것입니다.")
    @PostMapping("/unlink")
    public ResponseEntity<?> kakaoUnlink(@RequestParam String userId) {

        if (userId != null && !userId.isEmpty()) {
            try {
                String message = kakaoService.unlink(userId); // 계정과 연결 끊기 처리

                return ResponseEntity.ok().body(message); // 성공 시 userId 반환
            } catch (RuntimeException e) {
                return ResponseEntity.badRequest().body(e.getMessage()); // 예외 발생 시 에러 메시지 반환
            }
        } else {
            return ResponseEntity.badRequest().body("accessToken is null or empty"); // accessToken이 없는 경우 처리
        }
    }
}
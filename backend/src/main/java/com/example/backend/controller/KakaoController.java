package com.example.backend.controller;

import com.example.backend.dto.KakaoDto;
import com.example.backend.model.UsersEntity;
import com.example.backend.service.KakaoService;
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
}
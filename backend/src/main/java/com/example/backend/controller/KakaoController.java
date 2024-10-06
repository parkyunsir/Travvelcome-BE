package com.example.backend.controller;

import com.example.backend.dto.KakaoDto;
import com.example.backend.model.UsersEntity;
import com.example.backend.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


// 추후에 login.html 파일 지우고 리액트와 연결시키기

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoController {

    @Autowired
    private KakaoService kakaoService;

    @GetMapping("/frontend/callback") // 사용자 정보
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        KakaoDto userInfo = kakaoService.getUserInfo(accessToken);
        UsersEntity getUserEntity = KakaoDto.toEntity(userInfo);

        UsersEntity savedEntity = kakaoService.saveUser(getUserEntity);

        return ResponseEntity.ok().body(savedEntity);
    }
}
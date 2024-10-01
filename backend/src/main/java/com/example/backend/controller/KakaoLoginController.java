package com.example.backend.controller;

import com.example.backend.dto.KakaoDto;
import com.example.backend.model.UsersEntity;
import com.example.backend.service.KakaoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/login")
public class KakaoLoginController {

    @Value("${kakao.client.id}")
    private String client_id;

    @Value("${kakao.redirect.uri}")
    private String redirect_uri;

    @GetMapping
    public ResponseEntity<?> loginPage() {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+client_id+"&redirect_uri="+redirect_uri;
        // 카카오 인증 후 돌아올 때는 "code" 파라미터가 함께 옵니다.
        return ResponseEntity.ok(null);
    }
}
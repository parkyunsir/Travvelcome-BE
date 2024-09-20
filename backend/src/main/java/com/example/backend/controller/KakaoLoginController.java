package com.example.backend.controller;

import com.example.backend.dto.KakaoUserDto;
import com.example.backend.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


// 추후에 login.html 파일 지우고 리액트와 연결시키기

@Slf4j
//@RestController
@Controller
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {

    private final KakaoService kakaoService;

    @Value("${kakao.client.id}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    @GetMapping("/login") // 로그인 화면 & token 발급 과정
    public String loginPage(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id="+clientId+"&redirect_uri="+redirectUri;
        model.addAttribute("location", location);

        return "login";
    }

    @GetMapping("/callback") // redirect uri로 code 받기
    public ResponseEntity<?> callback() {

        return ResponseEntity.ok().build(); // 메인 화면이 될 예정
    }
}
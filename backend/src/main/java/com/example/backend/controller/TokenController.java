//package com.example.backend.controller;
//
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
//import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
//import org.springframework.security.oauth2.core.OAuth2AccessToken;
//import org.springframework.security.oauth2.core.user.OAuth2User;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//@RestController
//public class TokenController {
//
//    private final OAuth2AuthorizedClientService clientService;
//
//    public TokenController(OAuth2AuthorizedClientService clientService) {
//        this.clientService = clientService;
//    }
//
//    @GetMapping("/token")
//    public String getToken(@AuthenticationPrincipal OAuth2User oAuth2User) {
//        OAuth2AuthorizedClient client = clientService.loadAuthorizedClient(
//                "kakao",  // OAuth2 공급자 ID (kakao)
//                oAuth2User.getName()  // 사용자 식별자 (ID)
//        );
//
//        OAuth2AccessToken accessToken = client.getAccessToken();
//
//        System.out.println("토큰 " + accessToken);
//
//        return "Access Token: " + accessToken.getTokenValue();
//    }
//}
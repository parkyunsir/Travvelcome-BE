package com.example.backend.service;

import com.example.backend.dto.KakaoTokenResponseDto;
import com.example.backend.dto.KakaoDto;
import com.example.backend.model.UsersEntity;
import com.example.backend.repository.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Slf4j
@Service
public class KakaoService {

    private String clientId;
    private String logoutRedirectUri;
    private final String KAUTH_TOKEN_URL_HOST ;
    private final String KAUTH_USER_URL_HOST;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    public KakaoService(@Value("${kakao.client.id}") String clientId,
                        @Value("${kakao.logout.redirect.uri}") String logoutRedirectUri) {
        this.clientId = clientId;
        this.logoutRedirectUri = logoutRedirectUri;
        KAUTH_TOKEN_URL_HOST ="https://kauth.kakao.com";
        KAUTH_USER_URL_HOST = "https://kapi.kakao.com";
    }

    // 토큰 가져오기
    public String getAccessTokenFromKakao(String code) {

        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", clientId)
                        .queryParam("code", code)
                        .build(true))
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();

        log.info("Access Token ------> {}", kakaoTokenResponseDto.getAccessToken());
        return kakaoTokenResponseDto.getAccessToken();
    }

    // 사용자 정보 (/v2/user/me)
    public KakaoDto getUserInfo(String accessToken) {

        KakaoDto userInfo = WebClient.create(KAUTH_USER_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                //TODO : Custom Exception
                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> Mono.error(new RuntimeException("Invalid Parameter")))
                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> Mono.error(new RuntimeException("Internal Server Error")))
                .bodyToMono(KakaoDto.class)
                .block();

        log.info("ID ---> {} ", userInfo.getId());
//        log.info("Email ---> {} ", userInfo.getKakaoAccount().getEmail()); // 현재 이메일은 null 값이다.
        return userInfo;
    }

    // 로그아웃
    public String logout(String accessToken) {

        Long userId = WebClient.create(KAUTH_USER_URL_HOST)
                .post()
                .uri("/v1/user/logout")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new RuntimeException("Invalid Parameter: 4xx error")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new RuntimeException("Internal Server Error: 5xx error")))
                .bodyToMono(Long.class)
                .block();

        log.info("LOGOUT ID ---> {} ", userId);
//        log.info("Email ---> {} ", userInfo.getKakaoAccount().getEmail()); // 현재 이메일은 null 값이다.

        // 카카오 계정 세션 만료 처리
        WebClient.create(KAUTH_TOKEN_URL_HOST)
                .get()
                .uri(uriBuilder -> uriBuilder.path("/oauth/logout")
                        .queryParam("client_id", clientId)
                        .queryParam("logout_redirect_uri", logoutRedirectUri)
                        .build())
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new RuntimeException("Invalid Parameter for Account Logout: 4xx error")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new RuntimeException("Internal Server Error for Account Logout: 5xx error")))
                .toBodilessEntity()
                .block();

        return "로그아웃 되었습니다.";
    }

    // 계정 탈퇴
    public String unlink(String accessToken) {

        Long userId = WebClient.create(KAUTH_USER_URL_HOST)
                .post()
                .uri("/v1/user/unlink")
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .retrieve()
                .onStatus(HttpStatusCode::is4xxClientError,
                        clientResponse -> Mono.error(new RuntimeException("Invalid Parameter: 4xx error")))
                .onStatus(HttpStatusCode::is5xxServerError,
                        clientResponse -> Mono.error(new RuntimeException("Internal Server Error: 5xx error")))
                .bodyToMono(Long.class)
                .block();

        log.info("UNLINK ID ---> {} ", userId);

        return "계정 탙퇴 되었습니다.";
    }


    public UsersEntity saveUser(UsersEntity user) {
        return userRepository.save(user);
    }
}
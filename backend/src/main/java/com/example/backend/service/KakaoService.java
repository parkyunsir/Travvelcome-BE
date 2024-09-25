package com.example.backend.service;

import com.example.backend.dto.KakaoTokenResponseDto;
import com.example.backend.dto.KakaoUserDto;
import com.example.backend.model.UsersEntity;
import com.example.backend.repository.UsersRepository;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;


@Slf4j
@RequiredArgsConstructor
@Service
public class KakaoService {

    private String clientId;
    private final String KAUTH_TOKEN_URL_HOST ;
    private final String KAUTH_USER_URL_HOST;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    public KakaoService(@Value("${kakao.client.id}") String clientId) {
        this.clientId = clientId;
        KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
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
    public KakaoUserDto getUserInfo(String accessToken) {
        KakaoUserDto userInfo = WebClient.create(KAUTH_USER_URL_HOST)
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
                .bodyToMono(KakaoUserDto.class)
                .block();

        log.info("ID ---> {} ", userInfo.getId());
//        log.info("Email ---> {} ", userInfo.getKakaoAccount().getEmail()); // 현재 이메일은 null 값이다.
        return userInfo;
    }

    // 사용자 정보 저장
//    public KakaoUserDto saveUserInfo(String accessToken, KakaoUserDto userInfoToUpdate) {
//        // userInfoToUpdate는 수정할 사용자 정보를 담고 있어야 합니다.
//
//        KakaoUserDto updatedUserInfo = WebClient.create(KAUTH_USER_URL_HOST)
//                .post()
//                .uri(uriBuilder -> uriBuilder
//                        .scheme("https")
//                        .path("/v1/user/update_profile")
//                        .build(true))
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken) // access token 인가
//                .header(HttpHeaders.CONTENT_TYPE, "application/json") // JSON으로 설정
//                .bodyValue(userInfoToUpdate) // 요청 본문을 JSON으로 설정
//                .retrieve()
//                // TODO: Custom Exception
//                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
//                    log.error("Client Error: {}", clientResponse.bodyToMono(String.class).block());
//                    return Mono.error(new RuntimeException("Invalid Parameter"));
//                })
//                .onStatus(HttpStatusCode::is5xxServerError, clientResponse -> {
//                    log.error("Server Error: {}", clientResponse.bodyToMono(String.class).block());
//                    return Mono.error(new RuntimeException("Internal Server Error"));
//                })
//                .bodyToMono(KakaoUserDto.class)
//                .block();
//
//        log.info("Updated ID ---> {} ", updatedUserInfo.getId());
//        return updatedUserInfo;
//    }

//    @Transactional
//    public UsersEntity updateUserProfile(String accessToken, UsersEntity user) {
//        // Create a request body with the necessary fields
//        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
//        // Add the fields you want to update
//        body.add("nickname", user.getNickname());
//        body.add("profile_image", user.getProfileImageUrl());
//        body.add("thumbnail_image", user.getThumbnailImageUrl());
//
//        log.info("Updating user profile with data: {}", body);
//
//        // Make a request to the Kakao update profile API
//        ResponseEntity<String> response = WebClient.create(KAUTH_USER_URL_HOST)
//                .post()
//                .uri(uriBuilder -> uriBuilder
//                        .scheme("https")
//                        .path("/v1/user/update_profile")
//                        .build(true))
//                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
//                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
//                .bodyValue(body)
//                .retrieve()
//                // Handle response and error
//                .onStatus(HttpStatusCode::is4xxClientError, clientResponse -> {
//                    // Log the error details
//                    return clientResponse.bodyToMono(String.class)
//                            .flatMap(errorResponse -> {
//                                log.error("Error Response: {}", errorResponse);
//                                return Mono.error(new RuntimeException("Invalid Parameter: " + errorResponse));
//                            });
//                })
//                .onStatus(HttpStatusCode::is5xxServerError, clientResponse ->
//                        Mono.error(new RuntimeException("Internal Server Error")))
//                .toEntity(String.class)
//                .block();
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            log.info("User profile updated successfully.");
//        } else {
//            log.error("Failed to update user profile. Response: {}", response.getBody());
//            throw new RuntimeException("Failed to update user profile.");
//        }
//
//        // Optionally, save the updated user info to your database
//        usersRepository.save(user);
//
//        return user;
//    }


    // 사용자 정보 가져오기
//    public UsersEntity getUserInfo(Long id) {
//        return usersRepository.findById(id)
//                .orElseThrow(() -> new NoSuchElementException("User not found with id: " + id));
//    }

}
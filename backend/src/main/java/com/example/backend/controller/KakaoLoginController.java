package com.example.backend.controller;

import com.example.backend.dto.KakaoUserDto;
import com.example.backend.model.UsersEntity;
import com.example.backend.repository.UsersRepository;
import com.example.backend.service.KakaoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


// 추후에 login.html 파일 지우고 리액트와 연결시키기

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("")
public class KakaoLoginController {

    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private UsersRepository usersRepository;

    @GetMapping("/callback")
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        KakaoUserDto userInfo = kakaoService.getUserInfo(accessToken);

        UsersEntity entity = UsersEntity.builder()
                .id(userInfo.getId())
                .email(userInfo.getKakaoAccount().getEmail())
                .nickname(userInfo.getKakaoAccount().getProfile().getNickName())
                .thumbnailImageUrl(userInfo.getKakaoAccount().getProfile().getThumbnailImageUrl())
                .profileImageUrl(userInfo.getKakaoAccount().getProfile().getProfileImageUrl())
                .build();

        UsersEntity savedEntity = kakaoService.saveUserInfo(entity);

        // User 로그인, 또는 회원가입 로직 추가
        return ResponseEntity.ok().body(savedEntity); // 정상적으로 출력됨...
    }

    @GetMapping("/user") // 사용자 정보 출력
    public ResponseEntity<?> showUser(@AuthenticationPrincipal KakaoUserDto dto) {

        if (dto == null){
            log.info("dto is null: ");
        }

        Long id = KakaoUserDto.toEntity(dto).getId();


        // Optional로 반환받고, get() 메서드로 UsersEntity 가져오기
        Optional<UsersEntity> optionalUser = usersRepository.findById(id);

        if (optionalUser.isPresent()) {
            UsersEntity usersEntity = optionalUser.get();
            log.info(String.valueOf(usersEntity.getId()));
            return ResponseEntity.ok().body(usersEntity);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        }
    }
}
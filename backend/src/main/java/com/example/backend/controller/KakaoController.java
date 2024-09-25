package com.example.backend.controller;

import com.example.backend.dto.KakaoDto;
import com.example.backend.model.UsersEntity;
import com.example.backend.repository.UserRepository;
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

    @Autowired
    private UserRepository userRepository;

//    @Autowired
//    private UserOAuth2Service userOAuth2Service;

    @GetMapping("/callback") // 사용자 정보
    public ResponseEntity<?> callback(@RequestParam("code") String code) {
        String accessToken = kakaoService.getAccessTokenFromKakao(code);

        // 정보 출력
        KakaoDto getUserDTO = kakaoService.getUserInfo(accessToken);
        UsersEntity getUserEntity = KakaoDto.toEntity(getUserDTO);


//        UsersEntity setUserEntity = kakaoService.updateUserProfile(accessToken, getUserEntity);
//
//        // 저장하기
////        KakaoDto setUserDTO  = kakaoService.saveUserInfo(accessToken,getUserDTO);
//        log.info("setE: " + setUserEntity.toString());
//
//        KakaoDto savedDto = KakaoDto.builder()
//                .id(setUserEntity.getId())
//                .kakaoAccount(KakaoDto.KakaoAccount.builder()
////                        .email(setUserEntity.getEmail())
//                        .profile(KakaoDto.KakaoAccount.Profile.builder()
//                                .nickName(setUserEntity.getNickname())
//                                .thumbnailImageUrl(setUserEntity.getThumbnailImageUrl())
//                                .profileImageUrl(setUserEntity.getProfileImageUrl())
//                                .build())
//                        .build())
//                .build();

        // User 로그인, 또는 회원가입 로직 추가
        return ResponseEntity.ok().body(getUserEntity); // 정상적으로 출력됨... 근데 저장이 안 돼
    }

//    @GetMapping("/user") // 사용자 정보 출력
//    public ResponseEntity<?> showUser(@AuthenticationPrincipal KakaoDto dto) {
//
//        if (dto == null) {
//            log.info("dto is null: ");
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("User not authenticated");
//        }
//
//        log.info("User ID: " + dto.getId());
//        log.info("User Nickname: " + dto.getKakaoAccount().getProfile().getNickName());
//
//        Long id = KakaoDto.toEntity(dto).getId();
//
//
//        // Optional로 반환받고, get() 메서드로 UsersEntity 가져오기
//        UsersEntity usersEntity = kakaoService.getUserInfo(id);
//
//        if (usersEntity != null) {
//            log.info(String.valueOf(usersEntity.getId()));
//            return ResponseEntity.ok().body(usersEntity);
//        } else {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
//        }
//    }
}
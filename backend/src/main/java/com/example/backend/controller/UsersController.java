package com.example.backend.controller;

import com.example.backend.dto.UsersDTO;
import com.example.backend.model.UsersEntity;
import com.example.backend.service.UsersService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UsersController {

    @Autowired
    private final UsersService usersService;

    @GetMapping // 사용자 정보 출력
    public ResponseEntity<?> showUser(@AuthenticationPrincipal Long kakaoId) {

        UsersEntity userInfo = usersService.getUser(kakaoId); //id로 사용자 가져오기
        log.info("n: {}", userInfo.getNickname());
        log.info("k: {}", userInfo.getKakaoId());
        log.info("i: {}", userInfo.getId());
        UsersDTO dto = new UsersDTO(userInfo);

        return ResponseEntity.ok().body(dto);
    }

    /*
    @GetMapping("/one")
    public ResponseEntity<?> showTopicOne(@RequestParam("chatBotTopicId") Long chatBotTopicId) {
        ChatBotTopicEntity entity = chatBotTopicService.showOne(chatBotTopicId);
        ChatBotTopicDTO dto = new ChatBotTopicDTO(entity);
        return ResponseEntity.ok().body(dto);
    }
}
    }
    * */
}

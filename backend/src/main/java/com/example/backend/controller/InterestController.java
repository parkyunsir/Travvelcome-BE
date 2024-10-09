package com.example.backend.controller;


import com.example.backend.dto.ChatDTO;
import com.example.backend.dto.InterestDTO;
import com.example.backend.model.ChatEntity;
import com.example.backend.model.Interest;
import com.example.backend.model.Landmark;
import com.example.backend.model.UsersEntity;
import com.example.backend.model.enums.Category;
import com.example.backend.model.enums.Tag;
import com.example.backend.service.InterestService;
import com.example.backend.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/interest")
public class InterestController {

    @Autowired
    private InterestService interestService;

    @Autowired
    private KakaoService kakaoService;

    // 로그인 시 최초 관심사 등록
    @Operation(summary = "최초 관심사 등록 API", description = "최초 로그인 시, 관심사 등록할 수 있는 API입니다. RequestParam : userId, category 입니다! userId에 토큰을 입력해주세요.")
    @PostMapping()
    public ResponseEntity<?> addInterest(@RequestParam String userId, @RequestParam List<Category> categories) {
        Long id = kakaoService.getUserInfo(userId).getId();
        List<Interest> interests = interestService.addInterests(id, categories);
        List<InterestDTO> dtos = interests.stream().map(InterestDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

    // 태그별 관심사 (자연 / 지식 / 문화) 출력
    @GetMapping("/tag/{tag}")
    public ResponseEntity<?> getInterestsByTag(@PathVariable Tag tag) {
        List<Interest> interests = interestService.getTagInterest(tag);
        List<InterestDTO> dtos = interests.stream().map(InterestDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

    // 카테고리별 관심사
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getInterestsByCategory(@PathVariable Category category) {
        List<Interest> interests = interestService.getCategoryInterest(category);
        List<InterestDTO> dtos = interests.stream().map(InterestDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

    // 전체 관심사
    @GetMapping("/all")
    public ResponseEntity<?> getInterest(@PathVariable Category category) {
        List<Interest> interests = interestService.getAllInterest();
        List<InterestDTO> dtos = interests.stream().map(InterestDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }


    // 랜드마크 - 전체 관심사
    @GetMapping("/landmarks")
    public ResponseEntity<?> getLandmarksByInterest() {
        List<Landmark> landmarks = interestService.getAllInterestLandmark();
        return ResponseEntity.ok().body(landmarks);
    }
}

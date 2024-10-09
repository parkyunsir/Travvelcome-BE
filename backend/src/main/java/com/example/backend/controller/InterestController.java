package com.example.backend.controller;


import com.example.backend.dto.InterestDTO;
import com.example.backend.dto.KakaoDto;
import com.example.backend.model.Interest;
import com.example.backend.model.enums.Category;
import com.example.backend.model.enums.Tag;
import com.example.backend.service.InterestService;
import com.example.backend.service.KakaoService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/interest")
public class InterestController {

    @Autowired
    private InterestService interestService;

    @Autowired
    private KakaoService kakaoService;

    // 로그인 시 최초 관심사 등록
    @Operation(summary = "[개발중...] 최초 관심사 등록 API", description = "최초 로그인 시, 관심사 등록할 수 있는 API입니다. RequestPram userId에 토큰을 입력해주세요.")
    @PostMapping()
    public ResponseEntity<?> addInterest(@RequestBody InterestDTO interestDTO, @RequestParam String userId) {
        KakaoDto dto = kakaoService.getUserInfo(userId);
        Long id = dto.getId();

        Interest savedEntity = interestService.addInterests(id, interestDTO.getCategories());
        InterestDTO savedDTO = new InterestDTO(savedEntity);

        // userId와 savedDTO 묶기
        Map<String, Object> response = new HashMap<>();
        response.put("access token", userId); // 토큰
        response.put("savedDTO", savedDTO);

        return ResponseEntity.ok().body(response);
    }

    // 전체 관심사
    @Operation(summary = "[개발중...] 전체 관심사 출력 API", description = "전체 등록된 관심사를 출력할 수 있는 API입니다.")
    @GetMapping("/all")
    public ResponseEntity<?> getInterest(@RequestParam String userId) {
        KakaoDto userInfo = kakaoService.getUserInfo(userId);
        Long id = userInfo.getId();
        List<Interest> interests = null;
        List<InterestDTO> dtos = interests.stream().map(InterestDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "[개발중...] 태그별 관심사 출력 API", description = "자연 / 지식 / 문화 별로 관심사를 출력할 수 있는 API입니다.")
    // 태그별 관심사 (자연 / 지식 / 문화) 출력
    @GetMapping("/tag/{tag}")
    public ResponseEntity<?> getInterestsByTag(@PathVariable Tag tag) {
        List<Interest> interests = interestService.getTagInterest(tag);
        List<InterestDTO> dtos = interests.stream().map(InterestDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }

    @Operation(summary = "[개발중...] 카테고리별 관심사 출력 API", description = "산, 오름 ... 별로 관심사를 출력할 수 있는 API입니다.")
    // 카테고리별 관심사
    @GetMapping("/category/{category}")
    public ResponseEntity<?> getInterestsByCategory(@PathVariable Category category) {
        List<Interest> interests = interestService.getCategoryInterest(category);
        List<InterestDTO> dtos = interests.stream().map(InterestDTO::new).collect(Collectors.toList());
        return ResponseEntity.ok().body(dtos);
    }
}

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
import java.util.LinkedHashMap;
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
    @Operation(summary = "최초 관심사 등록 API", description = "최초 로그인 시, 관심사 등록할 수 있는 API입니다. RequestPram userId에 토큰을 입력해주세요." +
            " Request body에는 다음과 같이 입력해주세요. [\n" +
            "  { \"category\": \"MOUNTAIN\" },\n" +
            "  { \"category\": \"BEACH_ISLAND\" }\n" +
            "]")
    @PostMapping()
    public ResponseEntity<?> addInterest(@RequestBody List<InterestDTO> interests, @RequestParam String userId) {
        KakaoDto dto = kakaoService.getUserInfo(userId);
        Long id = dto.getId();

        List<Category> categories = interests.stream()
                .map(InterestDTO::getCategory)
                .collect(Collectors.toList());

        List<Interest> savedEntity = interestService.addInterests(id, categories);

        // List<Interest>에서 category만 추출하여 DTO로 변환
        List<InterestDTO> savedDTOs = savedEntity.stream()
            .map(interest -> new InterestDTO(
                interest.getCategory())) // category만 뽑아서 DTO로 만듦
            .collect(Collectors.toList());

        Map<String, Object> response = new LinkedHashMap<>(); // 순서가 userId - interest 순서
        response.put("userId", id);
        response.put("interest", savedDTOs);

        return ResponseEntity.ok().body(response);
    }

    // 현재 관심사
    @Operation(summary = "현재 관심사 출력 API", description = "현재 등록된 관심사를 출력할 수 있는 API입니다.")
    @GetMapping()
    public ResponseEntity<?> getInterest(@RequestParam String userId) {
        KakaoDto userInfo = kakaoService.getUserInfo(userId);
        Long id = userInfo.getId();

        List<Interest> interests = interestService.getAllInterest(id);

        List<InterestDTO> dtos = interests.stream().map(
                InterestDTO::new).collect(Collectors.toList());

        return ResponseEntity.ok().body(dtos);
    }

    // 관심사 전체 삭제
    @Operation(summary = "현재 관심사 전체 선택 해제 API", description = "현재 등록된 관심사를 전체 선택 해제 할 수 있는 API입니다. RequestPram userId에 토큰을 입력해주세요.")
    @DeleteMapping()
    public ResponseEntity<?> deleteInterests(@RequestParam String userId) {
        KakaoDto userInfo = kakaoService.getUserInfo(userId);
        Long id = userInfo.getId();
        interestService.deleteAllInterestsByUserId(id);
        return ResponseEntity.ok("All interests deleted for user " + id);
    }
}

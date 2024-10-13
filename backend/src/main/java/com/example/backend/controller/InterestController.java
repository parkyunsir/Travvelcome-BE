package com.example.backend.controller;

import com.example.backend.dto.InterestDTO;
import com.example.backend.dto.KakaoDto;
import com.example.backend.dto.LandmarkResponseDTO;
import com.example.backend.model.Interest;
import com.example.backend.model.Landmark;
import com.example.backend.model.enums.Category;
import com.example.backend.model.enums.Tag;
import com.example.backend.repository.LandmarkRepository;
import com.example.backend.service.CategoryService;
import com.example.backend.service.InterestService;
import com.example.backend.service.KakaoService;
import com.example.backend.service.LandmarkService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@Slf4j
@RequestMapping("/interest")
public class InterestController {

    @Autowired
    private InterestService interestService;

    @Autowired
    private KakaoService kakaoService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private LandmarkRepository landmarkRepository;

    @Autowired
    private LandmarkService landmarkService;

    // 로그인 시 최초 관심사 등록
    @Operation(summary = "최초 관심사 등록 API", description = "최초 로그인 시, 관심사 등록할 수 있는 API입니다. RequestPram userId에 토큰을 입력해주세요." +
            " Request body에는 다음과 같이 입력해주세요. [{ \"category\": \"MOUNTAIN\" }, { \"category\": \"BEACH_ISLAND\" } ]")
    @PostMapping()
    public ResponseEntity<?> addInterest(@RequestBody List<InterestDTO> interests, @RequestParam String userId) {
        KakaoDto dto = kakaoService.getUserInfo(userId);
        Long id = dto.getId();

        List<Category> categories = interests.stream()
                .map(InterestDTO::getCategory)
                .toList();

        List<Interest> savedEntity = interestService.addInterests(id, categories);

        // List<Interest>에서 category만 추출하여 DTO로 변환
        List<InterestDTO> savedDTOs = savedEntity.stream()
                .map(interest -> new InterestDTO(
                        interest.getCategory())) // category만 뽑아서 DTO로 만듦
                .toList();

        Map<String, Object> response = new LinkedHashMap<>(); // 순서가 userId - interest 순서
        response.put("userId", id);
        response.put("interest", savedDTOs);

        return ResponseEntity.ok().body(response);
    }

    // 현재 관심사
    @Operation(summary = "현재 관심사 출력 API", description = "현재 등록된 관심사를 출력할 수 있는 API입니다. category에 상응하는 tag도 출력됩니다. RequestPram userId에 토큰을 입력해주세요.")
    @GetMapping()
    public ResponseEntity<?> getInterest(@RequestParam String userId) {
        KakaoDto userInfo = kakaoService.getUserInfo(userId);
        Long id = userInfo.getId();

        List<Interest> interests = interestService.getAllInterest(id);

        List<Map<String, String>> response = interests.stream().map(interest -> {
            Map<String, String> categoryTagMap = new HashMap<>();
            categoryTagMap.put("category", interest.getCategory().toString());

            // 카테고리에 해당하는 첫 번째 tag 조회
            List<Tag> tags = categoryService.getTagsByCategory(interest.getCategory());
            String tagName = tags.isEmpty() ? null : tags.get(0).toString(); // 첫 번째 tag 가져오기
            categoryTagMap.put("tag", tagName);

            return categoryTagMap;
        }).toList();

        return ResponseEntity.ok().body(response);
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

    @Operation(summary = "[tag기반] 관심사로 등록된 관광지 출력 API", description = "현재 등록된 관심사를 기반으로 관광지를 출력합니다. tag(Nature)로 검색 시, MOUNTAIN, " +
            " BEACH_ISLAND, GARDEN, TRAIL, WATERFALL, DRIVE 중 관심사로 등록된 category에 해당하는 관광지가 출력됩니다.")
    @GetMapping("/landmark")
    // 관심사로 등록된 관광지 출력
    public ResponseEntity<?> getInterestLandmark(@RequestParam("userId") String userId, @RequestParam("tag") Tag tag) {

        KakaoDto Kdto = kakaoService.getUserInfo(userId);
        Long id = Kdto.getId();

        // 나의 관심사 목록에서 Category를 추출
        List<Interest> interests = interestService.getAllInterest(id);
        List<Category> interestCategories = interests.stream()
                .map(Interest::getCategory) // Category를 그대로 가져옴
                .toList();

        // tag에 해당하는 Category...
        List<Category> categories = categoryService.getCategoriesByTag(tag);

        List<Category> newCategories = categories.stream()
                .filter(interestCategories::contains)
                .toList();

        List<Landmark> result = landmarkRepository.findByCategories(newCategories);

        return ResponseEntity.ok().body(result);
    }

    @Operation(summary = "[모든] 관심사로 등록된 관광지 출력 API", description = "현재 등록된 관심사를 기반으로 관광지를 출력합니다. tag(Nature)로 검색 시, MOUNTAIN, " +
            " BEACH_ISLAND, GARDEN, TRAIL, WATERFALL, DRIVE 중 관심사로 등록된 category에 해당하는 관광지가 출력됩니다.")
    @GetMapping("/landmark/all")
    // 관심사로 등록된 관광지 출력
    public ResponseEntity<?> getAllInterestLandmark(@RequestParam("userId") String userId) {

        KakaoDto Kdto = kakaoService.getUserInfo(userId);
        Long id = Kdto.getId();

        // 나의 관심사 목록에서 Category를 추출
        List<Interest> interests = interestService.getAllInterest(id);
        List<Category> interestCategories = interests.stream()
                .map(Interest::getCategory) // Category를 그대로 가져옴
                .toList();

        List<Landmark> result = landmarkRepository.findByCategories(interestCategories);

        return ResponseEntity.ok().body(result);
    }
    
}

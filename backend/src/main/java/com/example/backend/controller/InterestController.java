//package com.example.backend.controller;
//
//
//import com.example.backend.dto.ChatDTO;
//import com.example.backend.dto.InterestDTO;
//import com.example.backend.model.ChatEntity;
//import com.example.backend.model.Interest;
//import com.example.backend.model.UsersEntity;
//import com.example.backend.model.enums.Category;
//import com.example.backend.model.enums.Tag;
//import com.example.backend.service.InterestService;
//import com.example.backend.service.KakaoService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
//@RestController
//@RequestMapping()
//public class InterestController {
//
//    @Autowired
//    private InterestService interestService;
//
//    @Autowired
//    private KakaoService kakaoService;
//
//    // 태그별 관심사 (자연 / 지식 / 문화) 출력
//    @GetMapping("/interest/tag/{tag}")
//    public ResponseEntity<?> getInterestsByTag(@PathVariable Tag tag) {
//        List<Interest> interests = interestService.getTagInterest(tag);
//        List<InterestDTO> dtos = interests.stream().map(InterestDTO::new).collect(Collectors.toList());
//        return ResponseEntity.ok().body(dtos);
//    }
//
//    // 카테고리별 관심사
//    @GetMapping("/interest/category/{category}")
//    public ResponseEntity<?> getInterestsByCategory(@PathVariable Category category) {
//        List<Interest> interests = interestService.getCategoryInterest(category);
//        List<InterestDTO> dtos = interests.stream().map(InterestDTO::new).collect(Collectors.toList());
//        return ResponseEntity.ok().body(dtos);
//    }
//
//    // 전체 관심사
//    @GetMapping("/interest/all")
//    public ResponseEntity<?> getInterest(@PathVariable Category category) {
//        List<Interest> interests = interestService.getAllInterest();
//        List<InterestDTO> dtos = interests.stream().map(InterestDTO::new).collect(Collectors.toList());
//        return ResponseEntity.ok().body(dtos);
//    }
//
//    // 관심사 등록
//    @PostMapping
//    public Interest saveInterest(@RequestParam Category category) {
//        return interestService.saveInterest(category);
//    }
//
//}

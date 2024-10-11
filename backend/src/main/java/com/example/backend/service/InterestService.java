package com.example.backend.service;

import com.example.backend.apiPayload.code.status.ErrorStatus;
import com.example.backend.apiPayload.exception.handler.TempHandler;
import com.example.backend.model.Interest;
import com.example.backend.model.UsersEntity;
import com.example.backend.model.enums.Category;
import com.example.backend.repository.InterestRepository;
import com.example.backend.repository.LandmarkRepository;
import com.example.backend.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class InterestService {

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private UserRepository userRepository;

    
    // 관심사 저장하기
    public List<Interest> addInterests(Long userId, List<Category> categories) {
        // 해당 유저를 조회
        UsersEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new TempHandler(ErrorStatus.USER_NOT_FOUND));

        List<Interest> interests = new ArrayList<>();

        for (Category category : categories) {
            // 이미 관심사로 등록 되었는지 check
            boolean exists = user.getInterests().stream()
                    .anyMatch(existingInterest -> existingInterest.getCategory() == category);

            if (!exists) {
                Interest interest = Interest.builder()
                        .user(user)
                        .category(category)
                        .build();

                user.getInterests().add(interest);
                interests.add(interest);
            }
        }

        interestRepository.saveAll(interests);
        userRepository.save(user);

        return interests; // 저장된 관심사 목록 반환
    }

    // 현재 등록된 관심사 불러오기
    public List<Interest> getAllInterest(Long userId) {
        return userRepository.findInterestsByUserId(userId);
    }

    // 현재 등록된 관심사 선택 해지
    public void deleteAllInterestsByUserId(Long userId) {
        interestRepository.deleteInterestsByUserId(userId);
    }
}

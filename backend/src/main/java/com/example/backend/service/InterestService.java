package com.example.backend.service;

import com.example.backend.apiPayload.code.status.ErrorStatus;
import com.example.backend.apiPayload.exception.handler.TempHandler;
import com.example.backend.model.Interest;
import com.example.backend.model.Landmark;
import com.example.backend.model.UsersEntity;
import com.example.backend.model.enums.Category;
import com.example.backend.model.enums.Tag;
import com.example.backend.repository.InterestRepository;
import com.example.backend.repository.LandmarkRepository;
import com.example.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterestService {

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private LandmarkRepository landmarkRepository;

    @Autowired
    private UserRepository userRepository;

    public Interest addInterests(Long userId, List<Category> categories) {
        // 해당 유저를 조회
        UsersEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new TempHandler(ErrorStatus.USER_NOT_FOUND));

        Interest interest = null;

        // 관심사 저장
        interest = Interest.builder()
            .user(user)
            .categories(categories)
            .build();

        user.getInterests().add(interest);

        interestRepository.save(interest);
        userRepository.save(user);

        return interest; // 저장된 관심사 목록 반환
    }

    /*
    *     public List<Interest> addInterests(Long userId, List<Category> categories) {
        // 해당 유저를 조회
        UsersEntity user = userRepository.findById(userId)
            .orElseThrow(() -> new TempHandler(ErrorStatus.USER_NOT_FOUND));

        List<Interest> interests = categories.stream()
            .map(category -> Interest.builder()
                .user(user) // `user`는 한 번만 설정됨
                .category(category)
                .build()
            )
            .collect(Collectors.toList());

        interestRepository.saveAll(interests);
        userRepository.save(user);

        return interests; // 저장된 관심사 목록 반환
    }
    *
    * */

    // 모든 관심사 불러오기
    public List<Interest> getAllInterest() {
        return interestRepository.findAll();
    }

    // tag 별로 불러오기
    public List<Interest> getTagInterest(Tag tag){
        return interestRepository.findByTag(tag);
    }

    // category 별로 불러오기
    public List<Interest> getCategoryInterest(Category category) {
        return interestRepository.findByCategory(category);
    }

    // 랜드마크 - 모두 불러오기
    public List<Landmark> getAllInterestLandmark() {

        // 모든 관심사
        List<Interest> interests =  interestRepository.findAll();

        // 관심사로 등록한 category 출력
        List<Category> categories = interests.stream()
                .map(Interest::getCategory)
                .collect(Collectors.toList());

        return landmarkRepository.findAll().stream()
                .filter(landmark -> landmark.getCategories().stream()
                        .anyMatch(categories::contains))
                .collect(Collectors.toList());
    }

    // 랜드마크 - tag 별로 불러오기
    public List<Interest> getTagInterestLandmark(Tag tag){
        return interestRepository.findByTag(tag);
    }

    // 랜드마크 - category 별로 불러오기
    public List<Interest> getCategoryInterestLandmark(Category category) {
        return interestRepository.findByCategory(category);
    }

}

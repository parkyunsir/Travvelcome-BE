package com.example.backend.service;

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

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InterestService {

    @Autowired
    private InterestRepository interestRepository;

    @Autowired
    private LandmarkRepository landmarkRepository;

    @Autowired
    private UserRepository usersRepository;

    // interest 등록하기
//    public Interest saveInterest(Category category) {
//        boolean interestExists = interestRepository.findByCategory(category).isEmpty();
//        if (!interestExists) {
//            throw new IllegalArgumentException("해당 카테고리는 이미 관심사로 등록되어 있습니다.");
//        }
//
//        Interest interest = Interest.builder()
////          .user(user)
//            .category(category)
//            .build();
//        return interestRepository.save(interest);
//    }
    public Interest addInterest(Long userId, Category category) {
        // 해당 유저를 조회
        UsersEntity user = usersRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        // 해당 관심사가 이미 존재하는지 확인
        boolean interestExists = interestRepository.findByUserIdAndCategory(userId, category).isEmpty();
        if (!interestExists) {
            throw new IllegalArgumentException("해당 카테고리는 이미 관심사로 등록되어 있습니다.");
        }

        // 새로운 관심사 생성 및 저장
        Interest interest = Interest.builder()
//                .userId(userId)
                .user(user)
                .category(category)
                .build();

        return interestRepository.save(interest);
    }

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

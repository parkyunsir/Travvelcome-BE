//package com.example.backend.service;
//
//import com.example.backend.model.Interest;
//import com.example.backend.model.UsersEntity;
//import com.example.backend.model.enums.Category;
//import com.example.backend.model.enums.Tag;
//import com.example.backend.repository.InterestRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.util.List;
//
//@Service
//public class InterestService {
//
//    @Autowired
//    private InterestRepository interestRepository;
//
//    // interest 등록하기
//    public Interest saveInterest(Category category) {
//        Interest interest = new Interest();
////        interest.setUser(user);
//        interest.setCategory(category);
//        return interestRepository.save(interest);
//    }
//
//    // tag 별로 불러오기
//    public List<Interest> getTagInterest(Tag tag){
//        return interestRepository.findByTag(tag);
//    }
//
//    // category 별로 불러오기
//    public List<Interest> getCategoryInterest(Category category) {
//        return interestRepository.findByCategory(category);
//    }
//
//    // 모든 관심사 불러오기
//    public List<Interest> getAllInterest() {
//        return interestRepository.findAll();
//    }
//
//}

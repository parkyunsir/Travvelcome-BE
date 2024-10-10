package com.example.backend.service;

import com.example.backend.apiPayload.code.status.ErrorStatus;
import com.example.backend.apiPayload.exception.handler.TempHandler;
import com.example.backend.model.Interest;
import com.example.backend.model.UsersEntity;
import com.example.backend.model.enums.Category;
import com.example.backend.repository.InterestRepository;
import com.example.backend.repository.StampRepository;
import com.example.backend.repository.UserRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService{

  private final UserRepository userRepository;
  private final InterestRepository interestRepository;
  private final StampRepository stampRepository;


  @Override
  public Long getUserStampCount(long userId) {
    UsersEntity user = userRepository.findById(userId).orElseThrow(() -> new TempHandler(ErrorStatus.USER_NOT_FOUND));
    return stampRepository.countByUser(user);
  }

  @Override
  @Transactional
  public void updateUserInterest(List<Category> categories, long userId) {
    UsersEntity user = userRepository.findById(userId).orElseThrow(() -> new TempHandler(ErrorStatus.USER_NOT_FOUND));
    // 기존 카테고리 삭제
    interestRepository.deleteByUserId(userId);

    // 새로운 카테고리 추가
    for (Category category : categories) {
      Interest interest = Interest.builder()
          .user(user)
          .category(category)
          .build();

      user.getInterests().add(interest);
    }

    userRepository.save(user);
  }
}

package com.example.backend.service;

import com.example.backend.apiPayload.code.status.ErrorStatus;
import com.example.backend.apiPayload.exception.handler.TempHandler;
import com.example.backend.model.UsersEntity;
import com.example.backend.repository.StampRepository;
import com.example.backend.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MyPageServiceImpl implements MyPageService{

  private final UsersRepository usersRepository;
  private final StampRepository stampRepository;


  @Override
  public Long getUserStampCount(long userId) {
    UsersEntity user = usersRepository.findById(userId).orElseThrow(() -> new TempHandler(ErrorStatus.USER_NOT_FOUND));
    return stampRepository.countByUser(user);
  }
}

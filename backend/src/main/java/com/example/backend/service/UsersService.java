package com.example.backend.service;

import com.example.backend.dto.KakaoUserDto;
import com.example.backend.dto.UsersDTO;
import com.example.backend.model.UsersEntity;
import com.example.backend.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UsersService {

    @Autowired
    private UsersRepository userRepository;

    // KakaoUserDto -> UserDTO로 변환
    public UsersDTO convertToUserDTO(KakaoUserDto kakaoUserDto) {
        UsersDTO usersDTO = new UsersDTO();
        usersDTO.setKakaoId(kakaoUserDto.getId());
        usersDTO.setNickname(kakaoUserDto.getProfile().getNickName());
        usersDTO.setProfileImageUrl(kakaoUserDto.getProfile().getProfileImageUrl());
        usersDTO.setThumbnailImageUrl(kakaoUserDto.getProfile().getThumbnailImageUrl());

        return usersDTO;
    }

    // 사용자 저장하기
    public UsersEntity saveUser(UsersEntity entity) {
        return userRepository.save(userRepository.findByKakaoId(entity.getKakaoId()));
    }

    // 사용자 정보 불러오기
    public UsersEntity getUser(Long kakaoId) {
        UsersEntity user = userRepository.findByKakaoId(kakaoId);

        return user;
    }





}

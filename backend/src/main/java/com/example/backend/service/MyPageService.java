package com.example.backend.service;

import com.example.backend.dto.MyPageRequestDTO.UpdateCategoryDto;
import com.example.backend.model.enums.Category;
import java.util.List;

public interface MyPageService {

  Long getUserStampCount(long userId);

  void updateUserInterest(List<Category> categories, long userId);
}

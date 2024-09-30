package com.example.backend.dto;

import com.example.backend.model.enums.Category;
import java.math.BigDecimal;
import java.util.List;
import lombok.Builder;
import lombok.Getter;

public class MyPageRequestDTO {

  @Getter
  @Builder
  public static class UpdateCategoryDto {
    List<Category> categoryList;
  }

}

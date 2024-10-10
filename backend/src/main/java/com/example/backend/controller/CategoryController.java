package com.example.backend.controller;

import com.example.backend.model.enums.Category;
import com.example.backend.service.InterestService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Autowired
    private InterestService interestService;

    // 모든 category 반환
    @Operation(summary = "모든 category 출력 API", description = "모든 category를 출력할 수 있는 API입니다.")
    @GetMapping("/all")
    public List<Category> getAllCategories() {
        // 서비스에서 카테고리 목록을 가져와 반환
        return interestService.getAllCategories();
    }
}

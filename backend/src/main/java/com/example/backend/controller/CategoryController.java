package com.example.backend.controller;

import com.example.backend.model.enums.Category;
import com.example.backend.model.enums.Tag;
import com.example.backend.service.CategoryService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping()
public class CategoryController {

    @Autowired
    private CategoryService categoryService;

    // 모든 tag 반환
    @Operation(summary = "모든 tag 출력 API", description = "모든 tag를 출력할 수 있는 API입니다.")
    @GetMapping("/tag/all")
    public List<Tag> getAllTags() {
        // 서비스에서 카테고리 목록을 가져와 반환
        return categoryService.getAllTags();
    }


    // 모든 category 반환
    @Operation(summary = "모든 category 출력 API", description = "모든 category를 출력할 수 있는 API입니다.")
    @GetMapping("/category/all")
    public List<Category> getAllCategories() {
        // 서비스에서 카테고리 목록을 가져와 반환
        return categoryService.getAllCategories();
    }

    @Operation(summary = "tag - category 출력 API", description = "tag를 검색했을 때 해당하는 모든 category를 출력할 수 있는 API입니다! ")
    @GetMapping("/tag/{tag}")
    public ResponseEntity<List<Category>> getCategoriesByTag(@PathVariable("tag") Tag tag) {
        List<Category> categories = categoryService.getCategoriesByTag(tag);
        return ResponseEntity.ok(categories);
    }

    @Operation(summary = "category - tag 출력 API", description = "category를 검색했을 때 해당하는 tag를 출력할 수 있는 API입니다! ")
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Tag>> getTagsByCategory(@PathVariable("category") Category category) {
        List<Tag> tags = categoryService.getTagsByCategory(category);
        return ResponseEntity.ok(tags);
    }
}

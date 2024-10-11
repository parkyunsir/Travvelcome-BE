package com.example.backend.service;

import com.example.backend.model.enums.Category;
import com.example.backend.model.enums.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class CategoryService {

    // 모든 category 불러오기
    public List<Category> getAllCategories() {
        // 모든 enum 값을 List로 반환
        return Arrays.asList(Category.values());
    }

    // 모든 tag 값 불러오기
    public List<Tag> getAllTags() {
        return Arrays.asList(Tag.values());
    }

    // Tag에 해당하는 Category 목록 반환
    public List<Category> getCategoriesByTag(Tag tag) {
        return tag.getCategories();
    }

    // Category -> Tag 맵핑 테이블
    private static final Map<Category, List<Tag>> categoryToTagsMap = new HashMap<>();

    static {
        // 각 Category에 해당하는 Tag를 매핑
        for (Tag tag : Tag.values()) {
            for (Category category : tag.getCategories()) {
                categoryToTagsMap
                        .computeIfAbsent(category, k -> new ArrayList<>())
                        .add(tag);
            }
        }
    }

    // Category에 해당하는 Tag 목록 반환
    public List<Tag> getTagsByCategory(Category category) {
        return categoryToTagsMap.getOrDefault(category, Collections.emptyList());
    }

}

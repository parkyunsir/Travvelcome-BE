package com.example.backend.model.enums;

import java.util.Arrays;
import java.util.List;

public enum Tag {
    // 자연 (Nature)
    NATURE(Arrays.asList(Category.MOUNTAIN, Category.BEACH_ISLAND, Category.GARDEN,
            Category.TRAIL, Category.WATERFALL, Category.DRIVE)),

    // 지식 (Knowledge)
    KNOWLEDGE(Arrays.asList(Category.HISTORY, Category.ECOLOGY_SCIENCE,
            Category.MYTH_LEGEND , Category.STORY_FIGURES)),

    // 문화 (Culture)
    CULTURE(Arrays.asList(Category.EXHIBITION, Category.ART, Category.CRAFT_EXPERIENCE ,
            Category.ACTIVITY , Category.THEME_PARK , Category.TASTE , Category.RELIGION));

    private final List<Category> categories;

    Tag(List<Category> categories) {
        this.categories = categories;
    }

    public List<Category> getCategories() {
        return categories;
    }
}
package com.github.psycomentis06.fxrepomain.events;

import com.github.psycomentis06.fxrepomain.repository.CategoryRepository;
import com.github.psycomentis06.fxrepomain.service.CategoryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CategoryCacheUpdateEventListener {
    private final CategoryService categoryService;
    private final CategoryRepository categoryRepository;

    public CategoryCacheUpdateEventListener(CategoryService categoryService, CategoryRepository categoryRepository) {
        this.categoryService = categoryService;
        this.categoryRepository = categoryRepository;
    }

    @EventListener
    public void onCategoryCacheUpdate(CategoryCacheUpdateEvent event) {
        log.info("CategoryCacheUpdateEventListener.onCategoryCacheUpdate");
        categoryRepository
                .findById(event.getCategoryId())
                .ifPresent(categoryService::refreshCategoryCache);
    }
}

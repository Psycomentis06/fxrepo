package com.github.psycomentis06.fxrepomain.service;

import com.github.psycomentis06.fxrepomain.entity.PostType;
import com.github.psycomentis06.fxrepomain.model.records.CategoryListModel;
import com.github.psycomentis06.fxrepomain.repository.CategoryRepository;
import com.github.psycomentis06.fxrepomain.repository.PostRepository;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;

@Service
public class CategoryService {

    private CategoryRepository categoryRepository;
    private PostRepository postRepository;
    private RedisTemplate<String, Object> redisTemplate;

    public static final String IMAGE_CATEGORY_LIST_KEY = "category:list";

    public CategoryService(CategoryRepository categoryRepository, PostRepository postRepository, RedisTemplate<String, Object> redisTemplate) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.redisTemplate = redisTemplate;
    }

    public Object getCategories(PostType type, String query, Pageable pageable) {
        var cachedData = redisTemplate.opsForValue().get(IMAGE_CATEGORY_LIST_KEY);
        if (cachedData != null) {
            return cachedData;
        }
        var categories = categoryRepository.findByNameContainsIgnoreCase(query, type, pageable);
        var catList = new ArrayList<CategoryListModel>();
        categories
                .get()
                .forEach(c -> {
                    var cat = new CategoryListModel(
                            c.getId(),
                            c.getName(),
                            c.getDescription(),
                            c.getSvgIcon(),
                            c.getThumbnail(),
                            c.getBgColor(),
                            c.getFgColor(),
                            c.getColor(),
                            postRepository.countPostsByCategory(c)
                    );
                    catList.add(cat);
                });
        var pageRes = new PageImpl<>(catList, pageable, categories.getTotalElements());
        // Cache for 12 hours as this an intensive operation
        redisTemplate.opsForValue().set(IMAGE_CATEGORY_LIST_KEY, pageRes, Duration.ofHours(12));
        return pageRes;
    }

    // Should be called implicitly to invalidate cache after some Add/Remove operation not after every Add/Remove
    public void invalidateCategoryListCache() {
        redisTemplate.delete(IMAGE_CATEGORY_LIST_KEY);
    }

}

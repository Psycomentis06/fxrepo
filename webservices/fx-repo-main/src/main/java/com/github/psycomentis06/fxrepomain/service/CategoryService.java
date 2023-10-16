package com.github.psycomentis06.fxrepomain.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.psycomentis06.fxrepomain.entity.Category;
import com.github.psycomentis06.fxrepomain.entity.PostType;
import com.github.psycomentis06.fxrepomain.model.records.CategoryListModel;
import com.github.psycomentis06.fxrepomain.repository.CategoryRepository;
import com.github.psycomentis06.fxrepomain.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CategoryService {

    private CategoryRepository categoryRepository;
    private PostRepository postRepository;
    private RedisTemplate<String, Object> redisTemplate;
    private ObjectMapper objectMapper;

    public static final String IMAGE_CATEGORY_LIST_KEY = "fx-repo:main-service:categories:list";
    public static final String IMAGE_CATEGORY_LIST_HASHMAP_TOTAL_NUMBER_KEY = "total-number";
    public static final String IMAGE_CATEGORY_LIST_HASHMAP_DATA_LIST_KEY = "data-list";
    public static final Duration CATEGORY_SET_EXPIRATION_DURATION = Duration.ofHours(12);

    public CategoryService(CategoryRepository categoryRepository, PostRepository postRepository, RedisTemplate<String, Object> redisTemplate, ObjectMapper objectMapper) {
        this.categoryRepository = categoryRepository;
        this.postRepository = postRepository;
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
    }

    public Object getCategories(PostType type, String query, Pageable pageable) {
        var categories = categoryRepository.findByNameContainsIgnoreCase(query, type, pageable);
        var catList = new ArrayList<CategoryListModel>();
        var cachedCategories = redisTemplate.opsForValue().multiGet(
                categories
                        .get()
                        .map(c -> IMAGE_CATEGORY_LIST_KEY + ":" + c.getId())
                        .toList()
        );
        if (cachedCategories == null) cachedCategories = new ArrayList<>();

        var cachedCategoriesMap = new HashMap<Integer, CategoryListModel>();
        cachedCategories.forEach(c -> {
            var category = (CategoryListModel) c;
            if (category != null) {
                cachedCategoriesMap.put(category.id(), category);
                catList.add(category);
            }
        });

        Map<String, Object> uncachedCategories = categories
                .get()
                .filter(c -> !cachedCategoriesMap.containsKey(c.getId()))
                .map(c -> {
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
                    return cat;
                })
                .collect(Collectors.toMap(
                        c -> IMAGE_CATEGORY_LIST_KEY + ":" + c.id(),
                        c -> c
                ));

        List<Object> pipelineCached = redisTemplate.execute(new SessionCallback<List<Object>>() {
            @Override
            public List<Object> execute(RedisOperations operations) throws DataAccessException {
                operations.multi();
                uncachedCategories.forEach((k, v) -> operations.opsForValue().set(k, v, CATEGORY_SET_EXPIRATION_DURATION));
                return operations.exec();
            }
        });
        if (pipelineCached != null) {
            log.info("Cached " + pipelineCached.size() + " categories");
        }
        return new PageImpl<>(catList, pageable, categories.getTotalElements());
    }

    public void refreshCategoryCache(Category c) {
        String categoryKey = IMAGE_CATEGORY_LIST_KEY + ":" + c.getId();
        CategoryListModel cat = new CategoryListModel(
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
        redisTemplate.opsForValue().set(categoryKey, cat, CATEGORY_SET_EXPIRATION_DURATION);
    }

    // Should be called implicitly to invalidate cache after some Add/Remove operation not after every Add/Remove
    public void invalidateCategoryListCache() {
        redisTemplate.delete(IMAGE_CATEGORY_LIST_KEY);
    }

}

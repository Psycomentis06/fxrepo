package com.github.psycomentis06.fxrepomain.events;

import com.github.psycomentis06.fxrepomain.entity.ImagePost;
import com.github.psycomentis06.fxrepomain.service.CategoryService;
import com.github.psycomentis06.fxrepomain.service.TypesenseService;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PostRemove;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PreUpdate;


public class ImagePostEntityEventHandler {
    private final TypesenseService typesenseService;
    private final CategoryService categoryService;

    public ImagePostEntityEventHandler(TypesenseService typesenseService, CategoryService categoryService) {
        this.typesenseService = typesenseService;
        this.categoryService = categoryService;
    }

    @PreUpdate
    public void perPersistImagePost(ImagePost imagePost) {
        typesenseService.addImagePostDocument(imagePost);
    }

    @PostPersist
    @PostUpdate
    @PostRemove
    public void postImageCrudEvent(ImagePost imagePost) {
        categoryService.refreshCategoryCache(imagePost.getCategory());
    }
}

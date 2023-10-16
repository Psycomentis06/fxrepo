package com.github.psycomentis06.fxrepomain.events;

import com.github.psycomentis06.fxrepomain.entity.Category;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import org.springframework.context.ApplicationEventPublisher;

public class CategoryEntityEventHandler {

    private final ApplicationEventPublisher applicationEventPublisher;

    CategoryEntityEventHandler(ApplicationEventPublisher applicationEventPublisher) {
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PrePersist
    @PreUpdate
    public void categoryCrudEvent(Category category) {
        applicationEventPublisher.publishEvent(new CategoryCacheUpdateEvent(this, category.getId()));
    }
}

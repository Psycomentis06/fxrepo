package com.github.psycomentis06.fxrepomain.events;

import org.springframework.context.ApplicationEvent;

public class CategoryCacheUpdateEvent extends ApplicationEvent {
    private int categoryId;

    CategoryCacheUpdateEvent(Object source, int categoryId) {
        super(source);
        this.categoryId = categoryId;
    }

    public int getCategoryId() {
        return categoryId;
    }
}

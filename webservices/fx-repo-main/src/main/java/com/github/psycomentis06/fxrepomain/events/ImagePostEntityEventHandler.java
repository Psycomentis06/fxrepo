package com.github.psycomentis06.fxrepomain.events;

import com.github.psycomentis06.fxrepomain.entity.ImagePost;
import com.github.psycomentis06.fxrepomain.service.TypesenseService;
import jakarta.persistence.PostUpdate;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreRemove;
import org.springframework.context.ApplicationEventPublisher;


public class ImagePostEntityEventHandler {
    private final TypesenseService typesenseService;
    private final ApplicationEventPublisher applicationEventPublisher;

    public ImagePostEntityEventHandler(TypesenseService typesenseService, ApplicationEventPublisher applicationEventPublisher) {
        this.typesenseService = typesenseService;
        this.applicationEventPublisher = applicationEventPublisher;
    }

    @PostUpdate
    public void postUpdateImagePost(ImagePost imagePost) {
        typesenseService.addImagePostDocument(imagePost);
    }

    @PrePersist
    @PreRemove
    public void postImageCrudEvent(ImagePost imagePost) {
        applicationEventPublisher
                .publishEvent(new CategoryCacheUpdateEvent(this, imagePost.getCategory().getId()));
    }
}

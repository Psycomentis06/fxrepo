package com.github.psycomentis06.fxrepomain.events;

import com.github.psycomentis06.fxrepomain.entity.ImagePost;
import com.github.psycomentis06.fxrepomain.service.TypesenseService;
import jakarta.persistence.PreUpdate;


public class ImagePostEntityEventHandler {
    private final TypesenseService typesenseService;

    public ImagePostEntityEventHandler(TypesenseService typesenseService) {
        this.typesenseService = typesenseService;
    }

    @PreUpdate
    public void perPersistImagePost(ImagePost imagePost) {
        typesenseService.addImagePostDocument(imagePost);
    }
}

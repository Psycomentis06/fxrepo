package com.github.psycomentis06.fxrepomain.events;

import com.github.psycomentis06.fxrepomain.entity.ImagePost;
import com.github.psycomentis06.fxrepomain.service.TypesenseService;
import jakarta.persistence.PostPersist;
import org.springframework.stereotype.Component;


@Component
public class ImagePostEntityEventHandler {
    private final TypesenseService typesenseService;

    public ImagePostEntityEventHandler(TypesenseService typesenseService) {
        this.typesenseService = typesenseService;
    }

    @PostPersist
    public void perPersistImagePost(ImagePost imagePost) {
        typesenseService.addImagePostDocument(imagePost);
    }
}

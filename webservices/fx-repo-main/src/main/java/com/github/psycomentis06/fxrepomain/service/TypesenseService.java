package com.github.psycomentis06.fxrepomain.service;

import com.github.psycomentis06.fxrepomain.entity.ImagePost;

public interface TypesenseService {
    String IMAGE_POST_COLLECTION_NAME = "ImagePost";

    void init();

    void createImagePostCollection();

    void addImagePostDocument(ImagePost imagePost);
}

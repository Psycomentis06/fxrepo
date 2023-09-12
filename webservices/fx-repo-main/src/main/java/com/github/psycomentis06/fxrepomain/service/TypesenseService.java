package com.github.psycomentis06.fxrepomain.service;

public interface TypesenseService {
    String IMAGE_POST_COLLECTION_NAME = "ImagePost";

    void init();

    void createImagePostCollection();
}

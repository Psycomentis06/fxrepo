package com.github.psycomentis06.fxrepomain.service;

import com.github.psycomentis06.fxrepomain.entity.ImagePost;
import org.typesense.model.SearchResult;

import java.util.Map;

public interface TypesenseService {
    String IMAGE_POST_COLLECTION_NAME = "ImagePost";

    void init();

    void createImagePostCollection();

    void addImagePostDocument(ImagePost imagePost);

    void updateImagePostDocument(ImagePost imagePost, boolean insertIfNotFound);

    void createImagePostDocument(Map<String, Object> doc);

    SearchResult getImagePosts(String query, int page, int perPage, String sortField, String direction);
}

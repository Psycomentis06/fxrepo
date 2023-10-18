package com.github.psycomentis06.fxrepomain.projection;

import com.github.psycomentis06.fxrepomain.entity.Tag;

import java.util.List;

public interface ImagePostListProjection {
    String getId();

    String getSlug();

    String getTitle();

    String getUserId();

    boolean isNsfw();

    List<Tag> getTags();

    CategoryImagePostListProjection getCategory();

    String getThumbnail();

    ImageFileImagePostListProjection getImage();

}

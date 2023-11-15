package com.github.psycomentis06.fxrepomain.projection;

import com.github.psycomentis06.fxrepomain.entity.Tag;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.List;

public interface ImagePostDetailsProjection {
        String getId();

    String getSlug();

    String getTitle();

    String getContent();
    LocalDateTime getCreatedAt();
    LocalDateTime getUpdatedAt();

    String getUserId();

    boolean isNsfw();

    List<Tag> getTags();

    CategoryImagePostListProjection getCategory();

    String getThumbnail();

    ImageFileImagePostListProjection getImage();

}

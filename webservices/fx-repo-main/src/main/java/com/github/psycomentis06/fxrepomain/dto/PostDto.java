package com.github.psycomentis06.fxrepomain.dto;

import com.github.psycomentis06.fxrepomain.entity.PostType;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
public class PostDto {

    private String id;
    private String slug;
    private String title;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private PostType postType;

    private String userId;
    private boolean publik = true;
    private boolean ready = false;
    private boolean nsfw;
    private Set<TagDto> tags;
    private CategoryDto category;
}

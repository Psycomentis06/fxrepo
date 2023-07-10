package com.github.psycomentis06.fxrepomain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.github.psycomentis06.fxrepomain.entity.Category;
import com.github.psycomentis06.fxrepomain.entity.Tag;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Set;

@Data
@NoArgsConstructor
@Accessors(chain = true)
public class ImagePostCreateModel {
    private String title;
    private String content;
    @JsonAlias("public")
    private boolean publik;
    private boolean nsfw;
    private Set<String> tags;
    private int category;
    private String image;
}

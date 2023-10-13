package com.github.psycomentis06.fxrepomain.model;

import com.fasterxml.jackson.annotation.JsonAlias;
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
    private String category;
    private String image;
}

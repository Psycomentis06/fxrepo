package com.github.psycomentis06.fxrepomain.model;

import com.github.psycomentis06.fxrepomain.entity.Tag;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TagListModel {
    private String tag;
    private Long posts;
    TagListModel(String tag, Long posts) {
        this.tag = tag;
        this.posts = posts;
    }
}

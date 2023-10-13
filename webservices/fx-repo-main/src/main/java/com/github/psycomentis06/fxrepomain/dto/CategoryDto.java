package com.github.psycomentis06.fxrepomain.dto;

import lombok.Data;

import java.util.Set;

@Data
public class CategoryDto {
    private int id;
    private String name;
    private String description;
    private String svgIcon;
    private String thumbnail;
    private String bgColor;
    private String fgColor;
    private String color;
    private Set<PostDto> posts;
}

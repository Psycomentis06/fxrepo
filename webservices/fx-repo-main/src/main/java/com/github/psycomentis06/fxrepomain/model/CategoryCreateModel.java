package com.github.psycomentis06.fxrepomain.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CategoryCreateModel {
    private String name;
    private String description;
    private String svgIcon;
    private String thumbnail;
    private String bgColor;
    private String fgColor;
    private String color;
}

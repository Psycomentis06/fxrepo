package com.github.psycomentis06.fxrepomain.model.records;

import java.io.Serializable;

public record CategoryListModel(
        int id,
        String name,
        String description,
        String svgIcon,
        String thumbnail,
        String bgColor,
        String fgColor,
        String color,
        Long posts
) implements Serializable {
}

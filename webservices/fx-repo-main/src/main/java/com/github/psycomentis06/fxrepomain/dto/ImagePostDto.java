package com.github.psycomentis06.fxrepomain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImagePostDto extends PostDto {
    private String thumbnail;
    private ImageFileDto image;
}

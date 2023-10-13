package com.github.psycomentis06.fxrepomain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
public class ImageFileDto extends FileDto {
    private String accentColor;
    private String colorPalette;
    private boolean landscape;
    private Set<ImagePostDto> posts;
}

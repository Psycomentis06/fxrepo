package com.github.psycomentis06.fxrepomain.projection;

import com.github.psycomentis06.fxrepomain.entity.FileVariant;

import java.util.Set;

public interface ImageFileGetProjection {
    String getId();
    Set<FileVariant> getVariants();
    String getAccentColor();
    String getColorPalette();
    boolean getLandscape();
}

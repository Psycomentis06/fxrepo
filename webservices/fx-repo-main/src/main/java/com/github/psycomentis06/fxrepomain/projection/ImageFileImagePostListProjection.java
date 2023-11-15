package com.github.psycomentis06.fxrepomain.projection;

import com.github.psycomentis06.fxrepomain.entity.FileVariant;

import java.util.List;

public interface ImageFileImagePostListProjection {
    String getId();

    String getAccentColor();

    String colorPalette();

    String isLandscape();

    List<FileVariant> getVariants();

    String getPerceptualHash();

    String getDifferenceHash();

    String getColorHash();

    String getAverageHash();
}

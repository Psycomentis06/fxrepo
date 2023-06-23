package com.github.psycomentis06.fxrepomain.entity;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@NoArgsConstructor
public class ImagePost extends Post {
    private String thumbnail;
    private String accentColor;
    private String colorPalette;
    private boolean nsfw;
    private boolean landscape;
    // variants
}

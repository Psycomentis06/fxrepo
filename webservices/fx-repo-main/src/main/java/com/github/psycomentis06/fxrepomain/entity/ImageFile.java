package com.github.psycomentis06.fxrepomain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.lang.annotation.Target;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "fx_image_file")
@NoArgsConstructor
@Data
public class ImageFile extends File {
    private String accentColor;
    private String colorPalette;
    private boolean landscape;
    @OneToMany(mappedBy = "image")
    private Set<ImagePost> posts;
}

package com.github.psycomentis06.fxrepomain.entity;

import com.github.psycomentis06.fxrepomain.events.ImagePostEntityEventHandler;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@EqualsAndHashCode(callSuper = true)
@EntityListeners(ImagePostEntityEventHandler.class)
@Entity
@Table(name = "fx_image_post")
@Data
@NoArgsConstructor
public class ImagePost extends Post {
    private String thumbnail;
    @ManyToOne(optional = false)
    private ImageFile image;
}

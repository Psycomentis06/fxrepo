package com.github.psycomentis06.fxrepomain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
public class ImagePostVariant {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private int id;
    // for the image that will be used as the original
    private boolean original = false;
    private int width;
    private int height;
    private int size;
    private String title;
    private String url;
    private String md5;
    private String sha256;
}

package com.github.psycomentis06.fxrepomain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "fx_file_variant")
@Data
@NoArgsConstructor
public class FileVariant {
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

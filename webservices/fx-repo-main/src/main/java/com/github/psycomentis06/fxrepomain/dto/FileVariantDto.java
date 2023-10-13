package com.github.psycomentis06.fxrepomain.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.github.psycomentis06.fxrepomain.jackson.IntegerDeserializer;
import lombok.Data;

@Data
public class FileVariantDto {
    @JsonDeserialize(using = IntegerDeserializer.class)
    private int id;
    private boolean original;
    private int width;
    private int height;
    private int size;
    private String title;
    private String url;
    private String md5;
    private String sha256;
}

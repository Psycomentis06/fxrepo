package com.github.psycomentis06.fxrepomain.dto;

import com.github.psycomentis06.fxrepomain.entity.FileServicePlacement;
import lombok.Data;

import java.util.Set;

@Data
public class FileDto {
    private String id;

    private FileServicePlacement placement;

    private boolean orphan;

    private Set<FileVariantDto> variants;

}

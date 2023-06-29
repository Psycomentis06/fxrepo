package com.github.psycomentis06.fxrepomain.controller;

import com.github.psycomentis06.fxrepomain.service.StorageService;
import com.github.psycomentis06.fxrepomain.service.StorageServiceStatus;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;

@RestController
@RequestMapping("/api/v1/image/")
public class StorageController {
    private StorageService storageService;

    @Autowired
    StorageController(StorageService storageService) {
        this.storageService = storageService;
    }
    @PostMapping("upload")
    public String upload(
            @RequestParam("file") MultipartFile file
    ){
        if (file == null) {
            return "File can't be null";
        }
        var f = storageService.store(file);
        switch (f) {
            case FILE_NOT_STORED -> {
                return "File not stored";
            }
            case FILE_STORED -> {
                return "File stored";
            }
            default -> {
                return "Default";
            }
        }
    }



    @GetMapping("{id}")
    public byte[] getImage(
            @PathVariable("id") String id
    ) throws IOException {
        return storageService.load(id);
    }
}

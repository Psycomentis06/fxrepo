package com.github.psycomentis06.fxrepomain.controller;

import com.github.psycomentis06.fxrepomain.model.ResponseObjModel;
import com.github.psycomentis06.fxrepomain.service.StorageService;
import com.github.psycomentis06.fxrepomain.service.StorageServiceStatus;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<ResponseObjModel> upload(
            @RequestParam("file") MultipartFile file
    ){
        var resObj = new ResponseObjModel();
        if (file == null) {
            resObj.setData(null)
                    .setMessage("\"file\" parameter is null")
                    .setCode(HttpStatus.BAD_REQUEST.value())
                    .setStatus(HttpStatus.BAD_REQUEST);
            return new ResponseEntity<>(resObj, HttpStatus.BAD_REQUEST);
        }
        var f = storageService.store(file);
        if (f == null) {
            resObj.setData(null)
                    .setMessage("File not stored")
                    .setCode(HttpStatus.FORBIDDEN.value())
                    .setStatus(HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(resObj, HttpStatus.FORBIDDEN);
        }
        resObj.setData(f)
                .setMessage("File stored")
                .setStatus(HttpStatus.OK)
                .setCode(HttpStatus.OK.value());
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    }



    @GetMapping("{id}")
    public byte[] getImage(
            @PathVariable("id") String id
    ) throws IOException {
        return storageService.load(id);
    }
}

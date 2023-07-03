package com.github.psycomentis06.fxrepomain.controller;

import com.github.psycomentis06.fxrepomain.entity.FileVariant;
import com.github.psycomentis06.fxrepomain.entity.ImageFile;
import com.github.psycomentis06.fxrepomain.model.ResponseObjModel;
import com.github.psycomentis06.fxrepomain.repository.FileVariantRepository;
import com.github.psycomentis06.fxrepomain.repository.ImageFileRepository;
import com.github.psycomentis06.fxrepomain.service.StorageService;
import com.github.psycomentis06.fxrepomain.util.Http;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import jakarta.websocket.server.PathParam;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/file/image")
public class ImageFileController {

    private StorageService storageService;
    private FileVariantRepository fileVariantRepository;
    private ImageFileRepository imageFileRepository;

    public ImageFileController(StorageService storageService, FileVariantRepository fileVariantRepository, ImageFileRepository imageFileRepository) {
        this.storageService = storageService;
        this.fileVariantRepository = fileVariantRepository;
        this.imageFileRepository = imageFileRepository;
    }

    @Transactional
    @PostMapping("/new")
    public ResponseEntity<ResponseObjModel> upload(
            @RequestParam(name = "file") MultipartFile img,
            HttpServletRequest servletRequest
            ) {

        var f = storageService.store(img);
        var resObj = new ResponseObjModel();
        if (f == null) {
            resObj.setData(null)
                    .setMessage("File not stored")
                    .setCode(HttpStatus.FORBIDDEN.value())
                    .setStatus(HttpStatus.FORBIDDEN);
            return new ResponseEntity<>(resObj, HttpStatus.FORBIDDEN);
        }

        FileVariant fileVariant = new FileVariant();
        fileVariant.setOriginal(true);
        fileVariant.setUrl(Http.getHostUrl(servletRequest) + "/api/v1/image/" + f);
        fileVariant.setTitle("Original");
        var variant = fileVariantRepository.save(fileVariant);
        ImageFile imageFile = new ImageFile();
        imageFile.setId(f);
        imageFile.setVariants(Collections.singleton(variant));
        var imgFile = imageFileRepository.save(imageFile);
        resObj.setData(imgFile)
                .setMessage("File stored")
                .setStatus(HttpStatus.CREATED)
                .setCode(HttpStatus.CREATED.value());
        return new ResponseEntity<>(resObj, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObjModel> getImageFile(@PathVariable String id) {
        var imgFile = imageFileRepository.findById(id);
        var obj = imgFile.orElseThrow(() -> new EntityNotFoundException("Image file not found"));
        ResponseObjModel o = new ResponseObjModel();
        o
                .setData(obj)
                .setMessage("Image file found")
                .setStatus(HttpStatus.OK)
                .setCode(HttpStatus.OK.value());
        return new ResponseEntity<>(o, HttpStatus.OK);
    }
}

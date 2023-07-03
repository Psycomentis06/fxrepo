package com.github.psycomentis06.fxrepomain.controller;

import com.github.psycomentis06.fxrepomain.entity.Category;
import com.github.psycomentis06.fxrepomain.entity.ImagePost;
import com.github.psycomentis06.fxrepomain.entity.Tag;
import com.github.psycomentis06.fxrepomain.model.ImagePostCreateModel;
import com.github.psycomentis06.fxrepomain.model.ResponseObjModel;
import com.github.psycomentis06.fxrepomain.repository.CategoryRepository;
import com.github.psycomentis06.fxrepomain.repository.FileVariantRepository;
import com.github.psycomentis06.fxrepomain.repository.ImageFileRepository;
import com.github.psycomentis06.fxrepomain.repository.ImagePostRepository;
import com.github.psycomentis06.fxrepomain.service.StorageService;
import com.github.psycomentis06.fxrepomain.service.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/post/image")
public class ImagePostController {

    private ImagePostRepository imagePostRepository;
    private TagService tagService;
    private CategoryRepository categoryRepository;
    private ImageFileRepository imageFileRepository;

    public ImagePostController(ImagePostRepository imagePostRepository, TagService tagService, CategoryRepository categoryRepository, ImageFileRepository imageFileRepository) {
        this.imagePostRepository = imagePostRepository;
        this.tagService = tagService;
        this.categoryRepository = categoryRepository;
        this.imageFileRepository = imageFileRepository;
    }

    @PostMapping("/new")
    public ResponseEntity<ResponseObjModel> createPost(
            @RequestBody ImagePostCreateModel postData
            ) {
        var imageFileOpt = imageFileRepository.findById(postData.getImage());
        var imageFile = imageFileOpt.orElseThrow(() -> new EntityNotFoundException("Image file not found"));
        ImagePost imagePost = new ImagePost();
        imagePost.setImage(imageFile);
        imagePost.setUserId("HelloHowAreYou");
        System.out.println(postData.getTags().stream().findFirst());
        Set<Tag> tags = new HashSet<>();
        postData.getTags().forEach(
                t -> {
                    var tag = tagService.getOrCreateTag(t);
                    tags.add(tag);
                }
        );
        imagePost.setTags(tags);
        Category category = categoryRepository.findById(postData.getCategory()).orElseThrow(EntityNotFoundException::new);
        imagePost.setCategory(category);
        imagePost.setTitle(postData.getTitle());
        imagePost.setContent(postData.getContent());
        imagePost.setPublik(postData.isPublik());
        imagePost.setNsfw(postData.isNsfw());

        var imp = imagePostRepository.save(imagePost);
        var resObj = new ResponseObjModel();
        resObj
                .setData(imp)
                .setMessage("Image post created")
                .setStatus(HttpStatus.CREATED)
                .setCode(HttpStatus.CREATED.value());
        return new ResponseEntity<>(resObj, resObj.getStatus());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ResponseObjModel> getPost(
            @PathVariable String id
    ) {
        var imgPost = imagePostRepository.findById(id);
        var obj = imgPost.orElseThrow(() -> new EntityNotFoundException("Post not found"));
        ResponseObjModel o = new ResponseObjModel();
        o
                .setData(obj)
                .setMessage("Post file found")
                .setStatus(HttpStatus.OK)
                .setCode(HttpStatus.OK.value());
        return new ResponseEntity<>(o, HttpStatus.OK);
    }

    public String getAll() {
        return "";
    }

    public String editPost() {
        return "";
    }
}

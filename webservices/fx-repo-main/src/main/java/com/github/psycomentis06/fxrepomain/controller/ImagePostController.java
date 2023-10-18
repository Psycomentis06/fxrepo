package com.github.psycomentis06.fxrepomain.controller;

import com.github.psycomentis06.fxrepomain.entity.*;
import com.github.psycomentis06.fxrepomain.model.ImagePostCreateModel;
import com.github.psycomentis06.fxrepomain.model.ResponseObjModel;
import com.github.psycomentis06.fxrepomain.model.records.ImagePostListModel;
import com.github.psycomentis06.fxrepomain.projection.ImagePostListProjection;
import com.github.psycomentis06.fxrepomain.repository.CategoryRepository;
import com.github.psycomentis06.fxrepomain.repository.ImageFileRepository;
import com.github.psycomentis06.fxrepomain.repository.ImagePostRepository;
import com.github.psycomentis06.fxrepomain.service.KafkaService;
import com.github.psycomentis06.fxrepomain.service.TagService;
import com.github.psycomentis06.fxrepomain.specification.ImagePostSpecification;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/v1/post/image")
public class ImagePostController {

    private ImagePostRepository imagePostRepository;
    private TagService tagService;
    private CategoryRepository categoryRepository;
    private ImageFileRepository imageFileRepository;
    private KafkaService kafkaService;

    public ImagePostController(ImagePostRepository imagePostRepository, TagService tagService, CategoryRepository categoryRepository, ImageFileRepository imageFileRepository, KafkaService kafkaService) {
        this.imagePostRepository = imagePostRepository;
        this.tagService = tagService;
        this.categoryRepository = categoryRepository;
        this.imageFileRepository = imageFileRepository;
        this.kafkaService = kafkaService;
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
        imagePost.setPostType(PostType.IMAGE);
        System.out.println(postData.getTags().stream().findFirst());
        Set<Tag> tags = new HashSet<>();
        postData.getTags().forEach(
                t -> {
                    var tag = tagService.getOrCreateTag(t);
                    tags.add(tag);
                }
        );
        imagePost.setTags(tags);
        Category category;
        try {
            int categoryId = Integer.parseInt(postData.getCategory());
            category = categoryRepository
                    .findById(categoryId)
                    .orElseThrow(() -> new EntityNotFoundException("Category with id %s not found".formatted(postData.getCategory())));
        } catch (NumberFormatException e) {
            category = categoryRepository
                    .findByNameIgnoreCase(postData.getCategory())
                    .orElseThrow(() -> new EntityNotFoundException("Category with name %s not found".formatted(postData.getCategory())));
        }
        imagePost.setCategory(category);
        imagePost.setTitle(postData.getTitle());
        imagePost.setContent(postData.getContent());
        imagePost.setPublik(postData.isPublik());
        imagePost.setNsfw(postData.isNsfw());
        var imp = imagePostRepository.save(imagePost);
        kafkaService.publishNewImagePostEvent(imp);
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

    @GetMapping("/list")
    public ResponseEntity<Object> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "nsfw", required = false, defaultValue = "false") boolean nsfw
    ) {
        Pageable pageable = PageRequest.of(page, limit);
//        var posts = imagePostRepository.findAllByTitleContains(ImagePostListProjection.class, search, pageable);
        Specification<ImagePost> imagePostSpecification = ImagePostSpecification
                .isPublic()
                .and(ImagePostSpecification.isReady())
//                .and(ImagePostSpecification.isNsfw(nsfw))
                .and(ImagePostSpecification.getByCategoryId(category))
                .and(ImagePostSpecification.getByTagName(tag))
                .and(
                        ImagePostSpecification.getByImageDescriptionContains(search)
                                .or(ImagePostSpecification.getByImageTitleContains(search)));
        var postsPage = imagePostRepository.findBy(imagePostSpecification, q -> q.as(ImagePostListProjection.class).page(pageable));
        List<ImagePostListModel> postsListModel = postsPage
                .stream()
                .map(p -> {
                    String imageUrl = p
                            .getImage()
                            .getVariants()
                            .stream()
                            .min(Comparator.comparing(FileVariant::getWidth).thenComparing(FileVariant::getHeight))
                            .orElseGet(null)
                            .getUrl();
                    if (imageUrl == null) return null;
                    return new ImagePostListModel(
                            p.getId(),
                            p.getTitle(),
                            p.getSlug(),
                            imageUrl,
                            p.getThumbnail(),
                            p.isNsfw(),
                            p.getCategory().getName(),
                            p.getTags().stream().map(Tag::getName).toList()
                    );
                })
                .toList();
        var postsNewPage = new PageImpl<>(postsListModel, pageable, postsPage.getTotalElements());
        return new ResponseEntity<>(postsNewPage, HttpStatus.OK);
    }
}

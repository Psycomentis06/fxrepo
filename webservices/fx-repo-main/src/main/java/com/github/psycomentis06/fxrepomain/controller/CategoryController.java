package com.github.psycomentis06.fxrepomain.controller;

import com.github.psycomentis06.fxrepomain.entity.Category;
import com.github.psycomentis06.fxrepomain.entity.PostType;
import com.github.psycomentis06.fxrepomain.model.CategoryCreateModel;
import com.github.psycomentis06.fxrepomain.model.ExceptionModel;
import com.github.psycomentis06.fxrepomain.model.ResponseObjModel;
import com.github.psycomentis06.fxrepomain.repository.CategoryRepository;
import com.github.psycomentis06.fxrepomain.service.CategoryService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    private CategoryRepository categoryRepository;
    private CategoryService categoryService;

    public CategoryController(CategoryRepository categoryRepository, CategoryService categoryService) {
        this.categoryRepository = categoryRepository;
        this.categoryService = categoryService;
    }

    @PostMapping("/new")
    public ResponseEntity<ResponseObjModel> create(
            @RequestBody CategoryCreateModel category
    ) {
        var c = new Category();
        c
                .setName(category.getName())
                .setColor(category.getColor())
                .setDescription(category.getDescription())
                .setBgColor(category.getBgColor())
                .setFgColor(category.getFgColor())
                .setThumbnail(category.getThumbnail())
                .setSvgIcon(category.getSvgIcon());
        var cat = categoryRepository.save(c);
        var res = new ResponseObjModel();
        res
                .setData(cat)
                .setMessage("Category created successfully")
                .setCode(HttpStatus.CREATED.value())
                .setStatus(HttpStatus.CREATED);
        return new ResponseEntity<>(res, res.getStatus());
    }

    @GetMapping("{type}/list")
    public ResponseEntity<Object> list(
            @PathVariable(value = "type") String type,
            @RequestParam(value = "p", defaultValue = "0") int page,
            @RequestParam(value = "l", defaultValue = "15") int limit,
            @RequestParam(value = "q", defaultValue = "") String query
    ) {
        PostType postType;
        try {
            postType = com.github.psycomentis06.fxrepomain.util.PostType.getPostType(type);
        } catch (IllegalArgumentException e) {
            var resErr = new ExceptionModel();
            resErr
                    .setTimestamp(new Timestamp(System.currentTimeMillis()))
                    .setStatus(HttpStatus.BAD_REQUEST)
                    .setCode(HttpStatus.BAD_REQUEST.value())
                    .setMessage(e.getMessage());
            return new ResponseEntity<>(resErr, resErr.getStatus());
        }
        var pageable = PageRequest.of(page, limit, Sort.Direction.ASC, "name");
//        var categories = categoryRepository.findByNameContainsIgnoreCase(CategoryListProjection.class, query, pageable);
        var categories = categoryService.getCategories(postType, query, pageable);
        var res = new ResponseObjModel();
        res
                .setData(categories)
                .setMessage("Done")
                .setStatus(HttpStatus.OK)
                .setCode(HttpStatus.OK.value());
        return new ResponseEntity<>(res, res.getStatus());
    }
}

package com.github.psycomentis06.fxrepomain.controller;

import com.github.psycomentis06.fxrepomain.entity.Category;
import com.github.psycomentis06.fxrepomain.model.CategoryCreateModel;
import com.github.psycomentis06.fxrepomain.model.ResponseObjModel;
import com.github.psycomentis06.fxrepomain.projection.CategoryListProjection;
import com.github.psycomentis06.fxrepomain.repository.CategoryRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
public class CategoryController {
    private CategoryRepository categoryRepository;

    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
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

    @GetMapping("/list")
    public ResponseEntity<Object> list(
            @RequestParam(value = "p", defaultValue = "0") int page,
            @RequestParam(value = "l", defaultValue = "15") int limit,
            @RequestParam(value = "s", defaultValue = "name") String sortAttribute,
            @RequestParam(value = "o", defaultValue = "asc") String sortDirection,
            @RequestParam(value = "q", defaultValue = "") String query
    ) {
        Sort.Direction sortD;
        if (sortDirection.equals("desc") || sortDirection.equals("d")) {
            sortD = Sort.Direction.DESC;
        } else {
            sortD = Sort.Direction.ASC;
        }
        var sort = Sort.by(sortD, sortAttribute);
        var pageable = PageRequest.of(page, limit, sort);
        var categories = categoryRepository.findByNameContainsIgnoreCase(CategoryListProjection.class, query, pageable);
        var res = new ResponseObjModel();
        res
                .setData(categories)
                .setMessage("Done")
                .setStatus(HttpStatus.OK)
                .setCode(HttpStatus.OK.value());
        return new ResponseEntity<>(res, res.getStatus());
    }
}

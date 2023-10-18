package com.github.psycomentis06.fxrepomain.controller;

import com.github.psycomentis06.fxrepomain.entity.PostType;
import com.github.psycomentis06.fxrepomain.model.ExceptionModel;
import com.github.psycomentis06.fxrepomain.model.ResponseObjModel;
import com.github.psycomentis06.fxrepomain.repository.PostRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;

@RestController
@RequestMapping("/api/v1/tag")
public class TagController {

    private PostRepository postRepository;

    public TagController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    @GetMapping("{type}/list")
    ResponseEntity<Object> listTags(
            @PathVariable() String type,
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
        var pageRequest = PageRequest.of(page, limit);
        var t = postRepository.findTagsByPostType(postType, query, pageRequest);
        var resObj = new ResponseObjModel();
        resObj
                .setData(t)
                .setMessage("Success")
                .setCode(HttpStatus.OK.value())
                .setStatus(HttpStatus.OK);
        return new ResponseEntity<>(resObj, resObj.getStatus());
    }
}

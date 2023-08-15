package com.github.psycomentis06.fxrepomain.controller.admin;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("adminImagePostController")
@RequestMapping("/api/v1/admin/post/image")
public class ImagePostController {

    public ResponseEntity<Object> getAll(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "search", required = false, defaultValue = "") String search,
            @RequestParam(value = "tag", required = false) String tag,
            @RequestParam(value = "category", required = false) String category,
            @RequestParam(value = "nsfw", required = false, defaultValue = "false") boolean nsfw
    ) {
        return ResponseEntity.ok("Get all for admin. Not implemented Yet");
    }
}

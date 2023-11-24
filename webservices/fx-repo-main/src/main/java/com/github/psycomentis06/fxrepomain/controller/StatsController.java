package com.github.psycomentis06.fxrepomain.controller;

import com.github.psycomentis06.fxrepomain.entity.FileDownloadEvent;
import com.github.psycomentis06.fxrepomain.entity.PostViewEvent;
import com.github.psycomentis06.fxrepomain.model.ResponseModel;
import com.github.psycomentis06.fxrepomain.model.ResponseObjModel;
import com.github.psycomentis06.fxrepomain.repository.*;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/stats/")
public class StatsController {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FileDownloadEventRepository fileDownloadEventRepository;
    private final PostViewEventRepository postViewEventRepository;
    private final PostRepository postRepository;

    public StatsController(UserRepository userRepository, FileRepository fileRepository, FileDownloadEventRepository fileDownloadEventRepository, PostViewEventRepository postViewEventRepository, PostRepository postRepository) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.fileDownloadEventRepository = fileDownloadEventRepository;
        this.postViewEventRepository = postViewEventRepository;
        this.postRepository = postRepository;
    }

    @PostMapping("/file/download")
    public ResponseEntity<Object> downloadFileStat(
            @RequestBody @Valid HashMap<String, String> payload,
            HttpServletRequest request
    ) {
        var fileOp = fileRepository.findById(payload.get("id"));
        var file = fileOp.orElseThrow(() -> new EntityNotFoundException("File not found"));
        var fileDownloadEvent = new FileDownloadEvent();
        fileDownloadEvent.setFile(file);
        try {
            var user = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            var userOp = userRepository.findByUsernameOrEmail(user.getUsername(), user.getUsername());
            userOp.ifPresent(fileDownloadEvent::setUser);
        } catch (ClassCastException ignored) {
        }
        var userAgent = request.getHeader("User-Agent");
        if (userAgent != null) fileDownloadEvent.setUserAgent(userAgent);
        fileDownloadEvent.setIpAddr(request.getRemoteAddr());
        var savedObj = fileDownloadEventRepository.save(fileDownloadEvent);
        var resHash = new HashMap<String, Object>();
        resHash.put("id", savedObj.getId());
        resHash.put("ipAddr", savedObj.getIpAddr());
        resHash.put("userAgent", savedObj.getUserAgent());
        resHash.put("createdAt", savedObj.getCreatedAt());
        resHash.put("fileId", payload.get("id"));
        var resObj = new ResponseObjModel();
        resObj.setData(resHash)
                .setCode(HttpStatus.OK.value())
                .setStatus(HttpStatus.OK)
                .setMessage("Event Saved Successfully");
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    }


    @GetMapping("/file/{id}")
    public ResponseEntity<Object> fileStats(
            @PathVariable() String id
    ) {
        return ResponseEntity.ok("");
    }


    @PostMapping("/post/view")
    public ResponseEntity<Object> viewFileStat(
            @RequestBody @Valid HashMap<String, String> payload,
            HttpServletRequest request
    ) {
        var userAgent = request.getHeader("User-Agent");
        var ipAddr = request.getRemoteAddr();
        var savedViewEventOp = postViewEventRepository.findByPostIdAndIpAddrEqualsIgnoreCaseAndUserAgentEqualsIgnoreCaseAndCreatedAtBetween(payload.get("id"), ipAddr, userAgent, LocalDateTime.now().minusDays(1), LocalDateTime.now());
        if (savedViewEventOp.isPresent()) {
            var resObj = new ResponseModel();
            resObj.setMessage("Already Created")
                    .setStatus(HttpStatus.ALREADY_REPORTED)
                    .setCode(HttpStatus.ALREADY_REPORTED.value());
            return new ResponseEntity<>(resObj, resObj.getStatus());
        }
        var postViewOp = postRepository.findById(payload.get("id"));
        var post = postViewOp.orElseThrow(() -> new EntityNotFoundException("File not found"));
        var postViewEvent = new PostViewEvent();
        postViewEvent.setPost(post);
        if (userAgent != null) postViewEvent.setUserAgent(userAgent);
        postViewEvent.setIpAddr(ipAddr);
        var savedObj = postViewEventRepository.save(postViewEvent);
        var resHash = new HashMap<String, Object>();
        resHash.put("id", savedObj.getId());
        resHash.put("ipAddr", savedObj.getIpAddr());
        resHash.put("userAgent", savedObj.getUserAgent());
        resHash.put("createdAt", savedObj.getCreatedAt());
        resHash.put("postId", payload.get("id"));
        var resObj = new ResponseObjModel();
        resObj.setData(resHash)
                .setCode(HttpStatus.OK.value())
                .setStatus(HttpStatus.OK)
                .setMessage("Event Saved Successfully");
        return new ResponseEntity<>(resObj, HttpStatus.OK);
    }
}

package com.github.psycomentis06.fxrepomain.controller;

import com.github.psycomentis06.fxrepomain.entity.FileDownloadEvent;
import com.github.psycomentis06.fxrepomain.model.ResponseObjModel;
import com.github.psycomentis06.fxrepomain.repository.FileDownloadEventRepository;
import com.github.psycomentis06.fxrepomain.repository.FileRepository;
import com.github.psycomentis06.fxrepomain.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

@RestController
@RequestMapping("/api/v1/stats/")
public class StatsController {

    private final UserRepository userRepository;
    private final FileRepository fileRepository;
    private final FileDownloadEventRepository fileDownloadEventRepository;

    public StatsController(UserRepository userRepository, FileRepository fileRepository, FileDownloadEventRepository fileDownloadEventRepository) {
        this.userRepository = userRepository;
        this.fileRepository = fileRepository;
        this.fileDownloadEventRepository = fileDownloadEventRepository;
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
        var ipAddr = request.getHeader("X-Forwarded-For");
        if (ipAddr != null) fileDownloadEvent.setIpAddr(ipAddr);
        else fileDownloadEvent.setIpAddr(request.getRemoteAddr());
        var savedObj = fileDownloadEventRepository.save(fileDownloadEvent);
        var resObj = new ResponseObjModel();
        resObj.setData(savedObj)
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


    @PostMapping("/file/{id}/view")
    public ResponseEntity<Object> viewFileStat() {

        return new ResponseEntity<>("Not Yet", HttpStatus.OK);
    }
}

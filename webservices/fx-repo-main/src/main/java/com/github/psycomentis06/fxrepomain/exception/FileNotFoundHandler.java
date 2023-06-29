package com.github.psycomentis06.fxrepomain.exception;

import com.github.psycomentis06.fxrepomain.model.ExceptionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.FileNotFoundException;
import java.sql.Timestamp;

@Slf4j(topic = "FileNotFoundExceptionHandler")
@RestControllerAdvice
public class FileNotFoundHandler {
    @ExceptionHandler(FileNotFoundException.class)
    public ResponseEntity<ExceptionModel> fileNotFoundException(FileNotFoundException e) {
        log.error(e.getMessage(), e);
        var m = new ExceptionModel();
        m
                .setCode(HttpStatus.NOT_FOUND.value())
                .setStatus(HttpStatus.NOT_FOUND)
                .setMessage(e.getMessage());
        m.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(m, HttpStatus.NOT_FOUND);
    }
}

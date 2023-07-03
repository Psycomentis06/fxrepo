package com.github.psycomentis06.fxrepomain.exception;

import com.github.psycomentis06.fxrepomain.model.ExceptionModel;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.FileNotFoundException;
import java.sql.Timestamp;

@Slf4j
@ControllerAdvice
public class EntityNotFoundExceptionHandler {
        @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ExceptionModel> fileNotFoundException(EntityNotFoundException e) {
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

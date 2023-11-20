package com.github.psycomentis06.fxrepomain.exception;

import com.github.psycomentis06.fxrepomain.model.ExceptionModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;

@Slf4j
@RestControllerAdvice
public class SecurityExceptionHandler {
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ExceptionModel> accessDenied(AccessDeniedException e) {
        log.error(e.getMessage(), e);
        var m = new ExceptionModel();
        m
                .setCode(HttpStatus.FORBIDDEN.value())
                .setStatus(HttpStatus.FORBIDDEN)
                .setMessage(e.getMessage());
        m.setTimestamp(new Timestamp(System.currentTimeMillis()));
        return new ResponseEntity<>(m, m.getStatus());
    }
}

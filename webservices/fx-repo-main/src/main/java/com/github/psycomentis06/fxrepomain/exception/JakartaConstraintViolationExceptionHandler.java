package com.github.psycomentis06.fxrepomain.exception;

import com.github.psycomentis06.fxrepomain.model.ExceptionViolationModel;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.Timestamp;
import java.util.HashMap;

@Slf4j
@RestControllerAdvice
public class JakartaConstraintViolationExceptionHandler {
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ExceptionViolationModel> violation(ConstraintViolationException e) {
        log.error(e.getMessage(), e);
        HashMap<String, String> map = new HashMap<>();
        e.getConstraintViolations().forEach(
                c -> map.put(c.getPropertyPath().toString(), c.getMessage())
        );
        var m = new ExceptionViolationModel();
        m
                .setConstraints(map)
                .setTimestamp(new Timestamp(System.currentTimeMillis()))
                .setCode(HttpStatus.BAD_REQUEST.value())
                .setStatus(HttpStatus.BAD_REQUEST)
                .setMessage("Failed to continue the operation due to constraints violation");
        return new ResponseEntity<>(m, m.getStatus());
    }
}

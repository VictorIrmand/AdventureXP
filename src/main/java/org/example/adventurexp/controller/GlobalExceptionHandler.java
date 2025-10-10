package org.example.adventurexp.controller;

import org.example.adventurexp.exception.DatabaseAccessException;
import org.example.adventurexp.exception.DuplicateResourceException;
import org.example.adventurexp.exception.NotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);


    // 400 BAD REQUEST
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidation(MethodArgumentNotValidException e) {
        logger.warn("Validation failed: {}", e.getMessage());

        List<String> errors = new ArrayList<>();
        e.getBindingResult().getFieldErrors().forEach(error ->
                errors.add(error.getDefaultMessage())
        );

        String error = errors.isEmpty() ? "Validation failed" : errors.getFirst();

        return ResponseEntity.badRequest().body(error);
    }

    // 500 INTERNAL SERVER ERROR
    @ExceptionHandler(DatabaseAccessException.class)
    public ResponseEntity<String> handleDatabaseAccessException(DatabaseAccessException e) {
        logger.error("Database error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(e.getMessage());
    }

    //  409 CONFLICT
    @ExceptionHandler(DuplicateResourceException.class)
    public ResponseEntity<String> handleDuplicateResource(DuplicateResourceException e) {
        logger.warn("Duplicate resource: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
    }

    // 404 NOT FOUND
    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<String> handleNotFound(NotFoundException e) {
        logger.warn("Resource not found: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    // 404 for static resource requests (Chrome / favicon osv.)
    @ExceptionHandler(org.springframework.web.servlet.resource.NoResourceFoundException.class)
    public ResponseEntity<Void> handleNoResourceFound() {
        return ResponseEntity.notFound().build();
    }

    // fallback if everythings fails
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleUnexpected(Exception e) {
        logger.error("Unexpected error: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An unexpected error occurred. Please try again later.");
    }
}

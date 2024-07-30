package com.cnsbd.pms.exceptionhandler;

import com.cnsbd.pms.auth.UsernameNotAvailableException;

import lombok.extern.slf4j.Slf4j;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<?> handleAuthenticationException(AuthenticationException e) {
        log.info("AuthenticationException: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatusCode.valueOf(400))
                .body("Invalid username or password");
    }

    @ExceptionHandler(UsernameNotAvailableException.class)
    public ResponseEntity<?> handleUsernameNotAvailableException(UsernameNotAvailableException e) {
        log.info("UsernameNotAvailableException: {}", e.getMessage(), e);
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body("Username not available");
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult()
                .getAllErrors()
                .forEach(
                        (error) -> {
                            String fieldName = ((FieldError) error).getField();
                            String errorMessage = error.getDefaultMessage();
                            errors.put(fieldName, errorMessage);
                        });
        return errors;
    }
}

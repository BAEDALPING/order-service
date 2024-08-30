package com.baedalping.delivery.global.common.exception;

import com.baedalping.delivery.global.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
  @ExceptionHandler(DeliveryApplicationException.class)
  public ResponseEntity<?> appException(final DeliveryApplicationException exception) {
    log.error("Error occurs in {}", exception.toString());
    return ResponseEntity.status(exception.getErrorCode().getStatus())
        .body(ApiResponse.error(exception.getErrorCode()));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<?> validationExceptionHandler(final MethodArgumentNotValidException e) {
    log.error("Validation error occurs in {}", e.toString());
    String[] errorMessages =
        e.getBindingResult().getFieldErrors().stream()
            .map(error -> String.format("[%s]: %s", error.getField(), error.getDefaultMessage()))
            .toArray(String[]::new);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiResponse.error(HttpStatus.BAD_REQUEST, errorMessages));
  }

  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<?> runtimeExceptionHandler(final RuntimeException exception) {
    log.error("Error occurs in {}", exception.toString());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiResponse.error(HttpStatus.INTERNAL_SERVER_ERROR, exception.getMessage()));
  }
}

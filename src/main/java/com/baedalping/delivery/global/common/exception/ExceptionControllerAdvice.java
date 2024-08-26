package com.baedalping.delivery.global.common.exception;

import com.baedalping.delivery.global.common.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class ExceptionControllerAdvice {
  @ExceptionHandler(DeliveryApplicationException.class)
  public ResponseEntity<?> runtimeExceptionHandler(final DeliveryApplicationException exception) {
    log.error("Error occurs in {}", exception.toString());
    return ResponseEntity.status(exception.getErrorCode().getStatus())
        .body(ApiResponse.error(exception.getErrorCode()));
  }
}

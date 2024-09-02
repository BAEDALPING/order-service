package com.baedalping.delivery.global.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class DeliveryApplicationException extends RuntimeException {
  private ErrorCode errorCode;

  @Override
  public String getMessage() {
    return errorCode.getMessage();
  }
}

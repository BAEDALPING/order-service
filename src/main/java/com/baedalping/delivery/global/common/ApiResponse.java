package com.baedalping.delivery.global.common;

import com.baedalping.delivery.global.common.exception.ErrorCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@NoArgsConstructor
public class ApiResponse<T> {
  private HttpStatus status;
  private Object message;
  private T data;

  private ApiResponse(HttpStatus status, Object message, T data) {
    this.status = status;
    this.message = message;
    this.data = data;
  }

  public static ApiResponse<Void> error(ErrorCode errorCode) {
    return new ApiResponse<>(errorCode.getStatus(), errorCode.getMessage(), null);
  }

  public static <T> ApiResponse<T> error(ErrorCode errorCode, T data) {
    return new ApiResponse<>(errorCode.getStatus(), errorCode.getMessage(), data);
  }

  public static ApiResponse<Void> error(HttpStatus httpStatus, Object message) {
    return new ApiResponse<>(httpStatus, message, null);
  }

  public static <T> ApiResponse<T> created(T data) {
    return new ApiResponse<>(HttpStatus.CREATED, null, data);
  }

  public static <T> ApiResponse<T> ok(T data) {
    return new ApiResponse<>(HttpStatus.OK, null, data);
  }
}

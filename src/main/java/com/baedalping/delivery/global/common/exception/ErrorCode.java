package com.baedalping.delivery.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "가입된 유저가 아닙니다"),

  NOT_FOUND_STORE_CATEGORY(HttpStatus.NOT_FOUND, "가게 분류를 찾을 수 없습니다."),
  DUPLICATE_STORE_CATEGORY_NAME(HttpStatus.NOT_FOUND, "가게 분류가 중복되었습니다."),

  NOT_FOUND_STORE(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."),

  DUPLICATE_PRODUCT_CATEGORY_NAME(HttpStatus.NOT_FOUND, "상품 분류가 중복되었습니다."),
  NOT_FOUND_PRODUCT_CATEGORY(HttpStatus.NOT_FOUND, "상품 분류를 찾을 수 없습니다.");

  private final HttpStatus status;
  private final String message;
}

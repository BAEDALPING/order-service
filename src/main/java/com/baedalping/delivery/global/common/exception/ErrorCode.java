package com.baedalping.delivery.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "가입된 유저가 아닙니다"),
  DUPLICATED_USER(HttpStatus.CONFLICT, "이미 가입된 유저 이메일 입니다"),
  ;

  private final HttpStatus status;
  private final String message;
}

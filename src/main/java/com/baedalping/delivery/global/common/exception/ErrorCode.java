package com.baedalping.delivery.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
  INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러"),

  NOT_FOUND_USER(HttpStatus.NOT_FOUND, "가입된 유저가 아닙니다"),
  DUPLICATED_USER(HttpStatus.CONFLICT, "이미 가입된 유저 이메일 입니다"),
  INVALID_PERMISSION(HttpStatus.UNAUTHORIZED, "요청 권한이 없습니다"),
  INVALID_PASSWORD(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다"),
  INVALID_TOKEN(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다"),
  EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다"),

  NOT_FOUND_STORE_CATEGORY(HttpStatus.NOT_FOUND, "가게 분류를 찾을 수 없습니다."),
  DUPLICATE_STORE_CATEGORY_NAME(HttpStatus.NOT_FOUND, "가게 분류가 중복되었습니다."),

  NOT_FOUND_STORE(HttpStatus.NOT_FOUND, "가게를 찾을 수 없습니다."),
  NOT_PERMITTED_OPTION(HttpStatus.NOT_FOUND, "해당 가게 또는 상품 생성자만 수정 삭제가 가능합니다."),

  NOT_FOUND_PRODUCT(HttpStatus.NOT_FOUND, "상품을 찾을 수 없습니다."),
  
  DUPLICATE_PRODUCT_CATEGORY_NAME(HttpStatus.NOT_FOUND, "상품 분류가 중복되었습니다."),
  NOT_FOUND_PRODUCT_CATEGORY(HttpStatus.NOT_FOUND, "상품 분류를 찾을 수 없습니다."),

  // Cart
  CART_ONLY_ONE_STORE_ALLOWED(HttpStatus.BAD_REQUEST, "한 번에 하나의 가게의 상품만 담을 수 있습니다."),
  NOT_FOUND_PRODUCT_IN_CART(HttpStatus.NOT_FOUND, "장바구니에 해당 상품이 존재하지 않습니다."),
  INVALID_PRODUCT_STORE_COMBINATION(HttpStatus.BAD_REQUEST, "상품과 가게가 올바른 조합이 아닙니다."),


  // Order
  NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "존재하지 않는 주문입니다."),
  CANNOT_CANCEL_ORDER_AFTER_5_MINUTES(HttpStatus.FORBIDDEN,"주문은 5분 이내에만 취소 가능합니다." ),
  ORDER_PERMISSION_DENIED(HttpStatus.UNAUTHORIZED, "해당 주문에 대한 권한이 없습니다." ),

  // Payment
  NOT_FOUND_PAYMENT(HttpStatus.NOT_FOUND, "존재하지 않는 결제내역입니다."),
  INVALID_PAYMENT_METHOD(HttpStatus.BAD_REQUEST,"유효하지 않은 결제 방식입니다."),
  PAYMENT_CANCELLATION_FAILED(HttpStatus.BAD_REQUEST,"현재 수정 불가한 결제입니다." ),
  PAYMENT_PERMISSION_DENIED(HttpStatus.UNAUTHORIZED,"해당 결제에 대한 권한이 없습니다." ),

  // Address
  NOT_FOUND_USER_ADDRESS(HttpStatus.NOT_FOUND, "존재하지 않는 주소입니다."),
  USER_ADDRESS_MISMATCH(HttpStatus.BAD_REQUEST, "해당 유저의 주소가 아닙니다."),

  // Review
  ALREADY_EXISTING_REVIEW(HttpStatus.BAD_REQUEST,"해당 주문에 대한 리뷰가 이미 생성되었습니다." ),
  NOT_FOUND_REVIEW(HttpStatus.NOT_FOUND,"리뷰를 찾을 수 없습니다.");  // 새로운 에러 코드 추가


  private final HttpStatus status;
  private final String message;
}

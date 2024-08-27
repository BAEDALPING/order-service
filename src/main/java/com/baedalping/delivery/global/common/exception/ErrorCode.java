package com.baedalping.delivery.global.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // User
    NOT_FOUND_USER(HttpStatus.NOT_FOUND, "가입된 유저가 아닙니다"),


    // Cart
    CART_ONLY_ONE_STORE_ALLOWED(HttpStatus.BAD_REQUEST, "한 번에 하나의 가게의 상품만 담을 수 있습니다."),
    NOT_FOUND_PRODUCT_IN_CART(HttpStatus.NOT_FOUND, "장바구니에 해당 상품이 존재하지 않습니다.");

    private final HttpStatus status;
    private final String message;
}

package com.baedalping.delivery.domain.cart.controller;

import com.baedalping.delivery.domain.cart.dto.CartRequestDto;
import com.baedalping.delivery.domain.cart.dto.CartResponseDto;
import com.baedalping.delivery.domain.cart.service.CartService;
import com.baedalping.delivery.global.common.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    // Authentication을 활용하기 이전의 임시 userId 설정
    private final String userId = "1";

    // 상품 추가
    @PostMapping("/add")
    public ApiResponse<CartResponseDto> addProductToCart(
        @RequestBody CartRequestDto cartProductDto) {
        return ApiResponse.created(
            new CartResponseDto(cartService.addProductToCart(userId, cartProductDto))
        );
    }

    // 장바구니 조회
//    @GetMapping
//    public ResponseEntity<List<CartProductDto>> getCartItems() {
//        List<CartItemDto> cartItems = cartService.getCartItems();
//        return ResponseEntity.ok(cartItems);
//    }

    // 상품 수량 변경
    @PatchMapping("/update")
    public ApiResponse<CartResponseDto> updateProductQuantity(
        @RequestParam String productId,
        @RequestParam int quantity) {
        return ApiResponse.ok(
            new CartResponseDto(cartService.updateProductQuantity(userId, productId, quantity))
        );
    }

    // 상품 단건 제거
    @DeleteMapping("/remove")
    public ApiResponse<CartResponseDto> removeItemFromCart(@RequestParam String productId) {
        return ApiResponse.ok(
            new CartResponseDto(cartService.removeItemFromCart(userId, productId))
        );
    }

    // 장바구니 비우기
    @DeleteMapping("/clear")
    public ApiResponse<CartResponseDto> clearCart() {
        return ApiResponse.ok(
            new CartResponseDto(cartService.clearCart(userId))
        );
    }
}


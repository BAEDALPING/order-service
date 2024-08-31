package com.baedalping.delivery.domain.cart.controller;

import com.baedalping.delivery.domain.cart.dto.CartProductDto;
import com.baedalping.delivery.domain.cart.dto.CartRequestDto;
import com.baedalping.delivery.domain.cart.dto.CartResponseDto;
import com.baedalping.delivery.domain.cart.service.CartService;
import com.baedalping.delivery.domain.user.dto.UserDetailsImpl;
import com.baedalping.delivery.global.common.ApiResponse;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
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

    // 상품 추가
    @PostMapping("/add")
    public ApiResponse<CartResponseDto> addProductToCart(
        @RequestBody CartRequestDto cartProductDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.created(
            new CartResponseDto(cartService.addProductToCart(
                String.valueOf(userDetails.getUserId()), cartProductDto)
            )
        );
    }

    // 장바구니 조회
    @GetMapping
    public ApiResponse<List<CartProductDto>> getCartItems(
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(cartService.getCartItems(String.valueOf(userDetails.getUserId())));
    }


    // 상품 수량 변경 + 상품의 개수를 0 이하로 설정 시 상품 삭제 기능 적용
    @PatchMapping("/update")
    public ApiResponse<CartResponseDto> updateProductQuantity(
        @RequestBody CartRequestDto cartProductDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ApiResponse.ok(
            new CartResponseDto(
                cartService.updateProductQuantity(String.valueOf(userDetails.getUserId()),
                    cartProductDto))
        );
    }

    // 장바구니 비우기
    @DeleteMapping("/clear")
    public ApiResponse<CartResponseDto> clearCart(
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return ApiResponse.ok(
            new CartResponseDto(cartService.clearCart(String.valueOf(userDetails.getUserId())))
        );
    }
}


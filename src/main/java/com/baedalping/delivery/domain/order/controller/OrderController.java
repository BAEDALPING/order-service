package com.baedalping.delivery.domain.order.controller;

import com.baedalping.delivery.domain.order.dto.OrderCreateRequestDto;
import com.baedalping.delivery.domain.order.dto.OrderCreateResponseDto;
import com.baedalping.delivery.domain.order.dto.OrderGetResponseDto;
import com.baedalping.delivery.domain.order.service.OrderService;
import com.baedalping.delivery.global.common.ApiResponse;
import com.baedalping.delivery.domain.user.dto.UserDetailsImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    public ApiResponse<OrderCreateResponseDto> createOrder(
        @RequestBody @Valid OrderCreateRequestDto orderRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails // 인증된 사용자 정보
    ) {
        return ApiResponse.created(
            orderService.createOrder(
                userDetails.getUserId(), // 인증된 사용자 ID 전달
                UUID.fromString(orderRequest.getAddressId()),
                orderRequest.getOrderType()
            )
        );
    }



    // 개인 주문 조회
    @GetMapping("/users")
    public ApiResponse<Page<OrderGetResponseDto>> getOrdersByUser(
        @AuthenticationPrincipal UserDetailsImpl userDetails,  // 인증된 사용자 정보
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size,
        @RequestParam(defaultValue = "asc") @Pattern(regexp = "asc|desc") String sortDirection
    ) {
        return ApiResponse.ok(
            orderService.getOrdersByUserId(userDetails.getUserId(), page, size, sortDirection)
        );
    }

    // 주문 단건 상세 조회
    @GetMapping("/{orderId}")
    public ApiResponse<OrderGetResponseDto> getOrderById(
        @PathVariable UUID orderId,
        @AuthenticationPrincipal UserDetailsImpl userDetails  // 인증된 사용자 정보
    ) {
        return ApiResponse.ok(
            orderService.getOrderById(orderId, userDetails.getUserId())
        );
    }

    // 주문 키워드 검색
    @GetMapping("/search")
    public ApiResponse<Page<OrderGetResponseDto>> searchOrders(
        @RequestParam @NotBlank String keyword,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size,
        @RequestParam(defaultValue = "asc") @Pattern(regexp = "asc|desc") String sortDirection) {
        return ApiResponse.ok(
            orderService.searchOrdersByKeyword(keyword, page, size, sortDirection));
    }

    // 주문 취소
    @DeleteMapping("/{orderId}/cancel")
    public ApiResponse<OrderGetResponseDto> cancelOrder(
        @PathVariable UUID orderId,
        @AuthenticationPrincipal UserDetailsImpl userDetails  // 인증된 사용자 정보
    ) {
        return ApiResponse.ok(orderService.cancelOrder(orderId, userDetails.getUserId()));
    }
}

package com.baedalping.delivery.domain.order.controller;

import com.baedalping.delivery.domain.order.dto.OrderCreateRequestDto;
import com.baedalping.delivery.domain.order.dto.OrderCreateResponseDto;
import com.baedalping.delivery.domain.order.dto.OrderGetResponseDto;
import com.baedalping.delivery.domain.order.service.OrderService;
import com.baedalping.delivery.global.common.ApiResponse;
import jakarta.validation.Valid;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

    /*
    TODO: body로 받아오고 있는 주문 상세 내역을 Redis에서 가져오도록 변경할 것
     */
    @PostMapping
    public ApiResponse<OrderCreateResponseDto> createOrder(
        @RequestBody @Valid OrderCreateRequestDto orderRequest) {
        return ApiResponse.created(
            orderService.createOrder(
                UUID.fromString(orderRequest.getAddressId()),
                orderRequest.getOrderType()
            )
        );
    }

    // 가게 주문 조회
    @GetMapping("/store/{storeId}")
    public ApiResponse<Page<OrderGetResponseDto>> getOrdersByStore(
        @PathVariable UUID storeId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "asc") String sortDirection
    ) {
        return ApiResponse.ok(
            orderService.getOrdersByStoreId(storeId, page, size, sortDirection)
        );
    }

    // 개인 주문 조회
    @GetMapping("/user/{userId}")
    public ApiResponse<Page<OrderGetResponseDto>> getOrdersByUser(
        @PathVariable Long userId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "asc") String sortDirection

    ) {
        return ApiResponse.ok(
            orderService.getOrdersByUserId(userId, page, size, sortDirection)
        );
    }

    // 주문 단건 상세 조회
    @GetMapping("/{orderId}")
    public ApiResponse<OrderGetResponseDto> getOrderById(@PathVariable UUID orderId) {
        return ApiResponse.ok(
            orderService.getOrderById(orderId)
        );
    }

    //
    // 주문 키워드 검색
    @GetMapping("/search")
    public ApiResponse<Page<OrderGetResponseDto>> searchOrders(
        @RequestParam String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(defaultValue = "asc") String sortDirection) {
        return ApiResponse.ok(
            orderService.searchOrdersByKeyword(keyword, page, size, sortDirection));
    }


    // 주문 취소
    @DeleteMapping("/{orderId}/cancel")
    public ApiResponse<OrderGetResponseDto> cancelOrder(@PathVariable UUID orderId) {
        return ApiResponse.ok(orderService.cancelOrder(orderId));
    }
}

package com.baedalping.delivery.domain.order.controller;

import com.baedalping.delivery.domain.order.dto.OrderGetResponseDto;
import com.baedalping.delivery.domain.order.service.OrderService;
import com.baedalping.delivery.global.common.ApiResponse;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner/orders")
@RequiredArgsConstructor
public class OwnerOrderController {
    private final OrderService orderService;

    // 가게 주문 조회
    @GetMapping("/stores/{storeId}")
    public ApiResponse<Page<OrderGetResponseDto>> getOrdersByStore(
        @PathVariable UUID storeId,
        @RequestParam(defaultValue = "0") @Min(0) int page,
        @RequestParam(defaultValue = "10") @Min(1) @Max(50) int size,
        @RequestParam(defaultValue = "asc") @Pattern(regexp = "asc|desc") String sortDirection
    ) {
        return ApiResponse.ok(
            orderService.getOrdersByStoreId(storeId, page, size, sortDirection)
        );
    }
}

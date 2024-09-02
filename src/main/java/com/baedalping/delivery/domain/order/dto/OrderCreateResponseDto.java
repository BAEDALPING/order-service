package com.baedalping.delivery.domain.order.dto;



import com.baedalping.delivery.domain.order.entity.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
public class OrderCreateResponseDto {
    private UUID orderId;
    private Long userId;
    private UUID storeId;
    private OrderStatus state;
    private Integer totalQuantity;
    private Integer totalPrice;
    private String shippingAddress;
    private Boolean isPublic;
    private List<OrderDetailResponseDto> orderDetails;

    public OrderCreateResponseDto setOrderDetails(List<OrderDetailResponseDto> orderDetails) {
        this.orderDetails = orderDetails;
        return this; // 'this'를 반환하여 메서드 체이닝이 가능하도록 수정
    }
}


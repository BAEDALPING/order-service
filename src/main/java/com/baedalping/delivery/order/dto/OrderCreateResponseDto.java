package com.baedalping.delivery.order.dto;



import com.baedalping.delivery.order.entity.OrderStatus;
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
    private List<OrderDetailResponseDto> orderDetails; // OrderDetailDTO로 변경

    // 기본 생성자, 필요시 커스텀 생성자 추가
}


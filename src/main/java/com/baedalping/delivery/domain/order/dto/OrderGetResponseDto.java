package com.baedalping.delivery.domain.order.dto;

import com.baedalping.delivery.domain.order.entity.OrderStatus;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class OrderGetResponseDto {
    private UUID orderId;
    private Long userId;
    private UUID storeId;
    private OrderStatus state;
    private Integer totalQuantity;
    private Integer totalPrice;
    private String shippingAddress;
    private Boolean isPublic;
    private List<OrderDetailResponseDto> orderDetails;

}

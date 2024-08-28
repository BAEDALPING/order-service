package com.baedalping.delivery.domain.order.dto;



import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class OrderDetailResponseDto {
    private UUID orderDetailId;
    private UUID productId;
    private String productName;
    private Integer quantity;
    private Integer unitPrice;
    private Integer subtotal;

}


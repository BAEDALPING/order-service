package com.baedalping.delivery.order.dto;



import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class OrderDetailResponseDto {
    private UUID orderDetailId;
    private UUID productId;
    private String productName;
    private Integer quantity;
    private Integer unitPrice;
    private Integer subtotal;

    // 기본 생성자, 필요시 커스텀 생성자 추가
}


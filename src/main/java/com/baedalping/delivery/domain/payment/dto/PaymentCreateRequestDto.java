package com.baedalping.delivery.domain.payment.dto;

import java.math.BigDecimal;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PaymentCreateRequestDto {

    // 외부 결제 모듈에서의 검증을 신뢰한다는 가정 valid 생략
    private Long userId;
    private UUID orderId;
    private String paymentMethod;
    private int totalAmount;

}

package com.baedalping.delivery.domain.payment.dto;

import com.baedalping.delivery.domain.order.entity.Order;
import com.baedalping.delivery.domain.payment.entity.Payment;
import com.baedalping.delivery.domain.payment.entity.PaymentState;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentResponseDto {

    private UUID paymentId;
    private Long userId;
    private UUID orderId;
    private String paymentMethod;
    private String cardNumber;  // 실제로는 보안 문제로 클라이언트에 전송하지 않는 것이 좋음
    private PaymentState state;
    private int totalAmount;
    private LocalDateTime paymentDate;
    private Boolean isPublic;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    // 정적 메서드 추가
    public static PaymentResponseDto fromEntity(Payment payment) {
        return PaymentResponseDto.builder()
            .paymentId(payment.getPaymentId())
            .userId(payment.getUserId())
            .orderId(payment.getOrder().getOrderId())  // Order의 orderId 필드 사용
            .paymentMethod(payment.getPaymentMethod())
            .cardNumber(null)  // 보안상의 이유로 cardNumber를 null로 설정하거나 마스킹
            .state(payment.getState())
            .totalAmount(payment.getTotalAmount())
            .paymentDate(payment.getPaymentDate())
            .isPublic(payment.getIsPublic())
            .createdAt(payment.getCreatedAt())
            .updatedAt(payment.getUpdatedAt())
            .deletedAt(payment.getDeletedAt())
            .build();
    }

    // 새로운 리스트 변환 메서드
    public static List<PaymentResponseDto> fromEntities(List<Payment> payments) {
        return payments.stream()
            .map(PaymentResponseDto::fromEntity)
            .collect(Collectors.toList());
    }
}

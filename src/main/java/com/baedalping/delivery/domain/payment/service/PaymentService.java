package com.baedalping.delivery.domain.payment.service;

import com.baedalping.delivery.domain.order.entity.Order;
import com.baedalping.delivery.domain.order.entity.OrderStatus;
import com.baedalping.delivery.domain.order.repository.OrderRepository;
import com.baedalping.delivery.domain.payment.dto.PaymentCreateRequestDto;
import com.baedalping.delivery.domain.payment.dto.PaymentResponseDto;
import com.baedalping.delivery.domain.payment.entity.Payment;
import com.baedalping.delivery.domain.payment.entity.PaymentState;
import com.baedalping.delivery.domain.payment.repository.PaymentRepository;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final OrderRepository orderRepository;

    @Transactional
    public PaymentResponseDto createPayment(PaymentCreateRequestDto paymentRequest) {
        if (!"CARD".equals(paymentRequest.getPaymentMethod())) {
            throw new DeliveryApplicationException(ErrorCode.INVALID_PAYMENT_METHOD);
        }

        Order order = orderRepository.findById(paymentRequest.getOrderId()).orElseThrow(
            () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_ORDER)
        );

        Payment payment = Payment.builder()
            .userId(paymentRequest.getUserId())
            .orderId(order)
            .paymentMethod(paymentRequest.getPaymentMethod())
            .totalAmount(paymentRequest.getTotalAmount())
            .paymentDate(LocalDateTime.now())
            .state(PaymentState.COMPLETE)
            .isPublic(true)
            .build();

        Payment savedPayment = paymentRepository.save(payment);
        order.setState(OrderStatus.CONFIRMED);
        orderRepository.flush();

        // 정적 팩토리 메서드를 사용하여 DTO 생성
        // TODO: mapper를 이용한 방법 사용해보기 (order의 순환참조로 인해 명시적 지정이 필요하지만 잘 안됨)
        return PaymentResponseDto.fromEntity(savedPayment);
    }

    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentById(UUID paymentId) {
        Payment payment = getPayment(paymentId);
        return PaymentResponseDto.fromEntity(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return PaymentResponseDto.fromEntities(payments);
    }

    @Transactional
    public PaymentResponseDto deletePayment(UUID paymentId) {
        // TODO: user 권한 체크
        Payment payment = getPayment(paymentId);
        payment.setState(PaymentState.CANCELLED);
        payment.setInvisible();
        payment.delete("1L");
        payment.getOrderId().setState(OrderStatus.CANCELLED);
        paymentRepository.save(payment);
        return PaymentResponseDto.fromEntity(payment);
    }

    private Payment getPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
            .orElseThrow(() -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_PAYMENT));
    }
}

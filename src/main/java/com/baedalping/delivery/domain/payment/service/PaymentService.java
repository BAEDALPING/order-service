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
    public PaymentResponseDto createPayment(PaymentCreateRequestDto paymentRequest, Long userId) {
        if (!"CARD".equals(paymentRequest.getPaymentMethod())) {
            throw new DeliveryApplicationException(ErrorCode.INVALID_PAYMENT_METHOD);
        }

        Order order = orderRepository.findById(paymentRequest.getOrderId()).orElseThrow(
            () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_ORDER)
        );

        // 사용자 ID가 주문의 사용자 ID와 일치하는지 확인
        if (!order.getUser().getUserId().equals(userId)) {
            throw new DeliveryApplicationException(ErrorCode.ORDER_PERMISSION_DENIED);
        }

        Payment payment = Payment.builder()
            .userId(userId) // 서비스 메서드에서 받은 사용자 ID 사용
            .order(order)
            .paymentMethod(paymentRequest.getPaymentMethod())
            .totalAmount(paymentRequest.getTotalAmount())
            .paymentDate(LocalDateTime.now())
            .state(PaymentState.COMPLETE)
            .isPublic(true)
            .build();

        Payment savedPayment = paymentRepository.save(payment);
        order.setState(OrderStatus.CONFIRMED);
        orderRepository.flush();

        return PaymentResponseDto.fromEntity(savedPayment);
    }

    @Transactional(readOnly = true)
    public PaymentResponseDto getPaymentById(UUID paymentId, Long userId) {
        Payment payment = getPayment(paymentId);

        // 사용자 ID가 결제의 사용자 ID와 일치하는지 확인
        if (!payment.getUserId().equals(userId)) {
            throw new DeliveryApplicationException(ErrorCode.PAYMENT_PERMISSION_DENIED);
        }

        return PaymentResponseDto.fromEntity(payment);
    }

    @Transactional(readOnly = true)
    public List<PaymentResponseDto> getAllPayments(Long userId) {
        // TODO: 유저 권한에 따라 모든 결제를 조회할 수 있는지 확인
        List<Payment> payments = paymentRepository.findAllByUserId(userId);
        return PaymentResponseDto.fromEntities(payments);
    }

    @Transactional
    public PaymentResponseDto cancelPayment(UUID paymentId, Long userId) {
        Payment payment = getPayment(paymentId);

        // 사용자 ID가 결제의 사용자 ID와 일치하는지 확인
        if (!payment.getUserId().equals(userId)) {
            throw new DeliveryApplicationException(ErrorCode.PAYMENT_PERMISSION_DENIED);
        }

        payment.setState(PaymentState.CANCELLED);
        payment.setInvisible();
        payment.getOrder().setState(OrderStatus.CANCELLED);
        paymentRepository.save(payment);
        return PaymentResponseDto.fromEntity(payment);
    }

    private Payment getPayment(UUID paymentId) {
        return paymentRepository.findById(paymentId)
            .orElseThrow(() -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_PAYMENT));
    }
}

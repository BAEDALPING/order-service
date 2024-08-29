package com.baedalping.delivery.domain.payment.controller;

import com.baedalping.delivery.domain.payment.dto.PaymentCreateRequestDto;
import com.baedalping.delivery.domain.payment.dto.PaymentResponseDto;
import com.baedalping.delivery.domain.payment.service.PaymentService;
import com.baedalping.delivery.global.common.ApiResponse;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping
    public ApiResponse<PaymentResponseDto> createPayment(
        @RequestBody PaymentCreateRequestDto paymentRequestDto) {
        return ApiResponse.created(paymentService.createPayment(paymentRequestDto));
    }

    @GetMapping("/{paymentId}")
    public ApiResponse<PaymentResponseDto> getPayment(@PathVariable UUID paymentId) {
        return ApiResponse.ok(paymentService.getPaymentById(paymentId));
    }

    @GetMapping
    public ApiResponse<List<PaymentResponseDto>> getAllPayments() {
        // TODO: 개인 주문 전체조회로 변경예정 - user 권한 이후
        return ApiResponse.ok(paymentService.getAllPayments());
    }

    @DeleteMapping("/{paymentId}")
    public ApiResponse<PaymentResponseDto> deletePayment(@PathVariable UUID paymentId) {
        // TODO: Admin 권한으로 설정할 예정 - 일반 유저는 Order 취소에서 구현
        return ApiResponse.ok(paymentService.deletePayment(paymentId));
    }
}

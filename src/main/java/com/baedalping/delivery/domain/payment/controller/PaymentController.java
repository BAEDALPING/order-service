package com.baedalping.delivery.domain.payment.controller;

import com.baedalping.delivery.domain.payment.dto.PaymentCreateRequestDto;
import com.baedalping.delivery.domain.payment.dto.PaymentResponseDto;
import com.baedalping.delivery.domain.payment.service.PaymentService;
import com.baedalping.delivery.global.common.ApiResponse;
import com.baedalping.delivery.domain.user.dto.UserDetailsImpl;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
        @RequestBody PaymentCreateRequestDto paymentRequestDto,
        @AuthenticationPrincipal UserDetailsImpl userDetails // 인증된 사용자 정보 추가
    ) {
        // 사용자 ID를 별도로 서비스 메서드로 전달
        return ApiResponse.created(paymentService.createPayment(paymentRequestDto, userDetails.getUserId()));
    }

    @GetMapping("/{paymentId}")
    public ApiResponse<PaymentResponseDto> getPayment(
        @PathVariable UUID paymentId,
        @AuthenticationPrincipal UserDetailsImpl userDetails // 인증된 사용자 정보 추가
    ) {
        return ApiResponse.ok(paymentService.getPaymentById(paymentId, userDetails.getUserId()));
    }

    @GetMapping
    public ApiResponse<List<PaymentResponseDto>> getAllPayments(
        @AuthenticationPrincipal UserDetailsImpl userDetails // 인증된 사용자 정보 추가
    ) {
        return ApiResponse.ok(paymentService.getAllPayments(userDetails.getUserId()));
    }

    @DeleteMapping("/{paymentId}")
    public ApiResponse<PaymentResponseDto> deletePayment(
        @PathVariable UUID paymentId,
        @AuthenticationPrincipal UserDetailsImpl userDetails // 인증된 사용자 정보 추가
    ) {
        return ApiResponse.ok(paymentService.cancelPayment(paymentId, userDetails.getUserId()));
    }
}

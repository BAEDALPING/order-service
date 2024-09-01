package com.baedalping.delivery.domain.review.service;

import com.baedalping.delivery.domain.order.entity.Order;
import com.baedalping.delivery.domain.order.repository.OrderRepository;
import com.baedalping.delivery.domain.review.dto.ReviewRequestDto;
import com.baedalping.delivery.domain.review.entity.Review;
import com.baedalping.delivery.domain.review.repository.ReviewRepository;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;


    public Review createReview(ReviewRequestDto reviewRequest, Long userId) {
        // 주문 확인 및 리뷰가 이미 존재하는지 검증
        Order order = orderRepository.findById(UUID.fromString(reviewRequest.getOrderId()))
            .orElseThrow(() -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_ORDER));

        // 현재 사용자 ID와 주문의 사용자 ID가 일치하는지 확인
        if (!order.getUser().getUserId().equals(userId)) {
            throw new DeliveryApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        if (order.getReview() != null) {
            throw new DeliveryApplicationException(ErrorCode.ALREADY_EXISTING_REVIEW);
        }

        // 리뷰 생성 및 저장
        Review review = Review.builder()
            .order(order)
            .user(order.getUser())
            .store(order.getStore())
            .rating(reviewRequest.getRating())
            .comment(reviewRequest.getComment())
            .isReported(false)
            .build();

        // 주문에 리뷰 연결
        order.setReview(review);

        return reviewRepository.save(review);
    }

}


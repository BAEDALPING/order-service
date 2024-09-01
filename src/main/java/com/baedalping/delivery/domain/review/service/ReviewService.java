package com.baedalping.delivery.domain.review.service;

import com.baedalping.delivery.domain.order.entity.Order;
import com.baedalping.delivery.domain.order.repository.OrderRepository;
import com.baedalping.delivery.domain.review.dto.ReportReviewRequestDto;
import com.baedalping.delivery.domain.review.dto.ReviewRequestDto;
import com.baedalping.delivery.domain.review.entity.Review;
import com.baedalping.delivery.domain.review.repository.ReviewRepository;
import com.baedalping.delivery.domain.store.repository.StoreRepository;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final StoreRepository storeRepository;
    private final OrderRepository orderRepository;
    private final ReviewRepository reviewRepository;
    private final CacheManager cacheManager; // CacheManager를 주입받습니다.


    @Transactional
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

        // 트랜잭션이 완료된 후 캐시 무효화 수행
        clearAverageRatingCache(order.getStore().getStoreId());

        return reviewRepository.save(review);
    }

    public void clearAverageRatingCache(UUID storeId) {
        // CacheManager를 사용하여 직접 캐시 무효화
        Objects.requireNonNull(cacheManager.getCache("averageRatingCache")).evict(storeId);
    }

    public List<Review> getReviewsByStore(UUID storeId) {
        // 신고된 리뷰 제외하고 조회
        existStoreId(storeId);
        return reviewRepository.findByStore_StoreIdAndIsReportedFalse(storeId);
    }

    public List<Review> getAllReviewsByStore(UUID storeId) {
        // 관리자용 신고여부 상관없는 전체 조회
        existStoreId(storeId);
        return reviewRepository.findByStore_StoreId(storeId);
    }

    public Review reportReview(UUID reviewId, ReportReviewRequestDto reportRequest, Long userId) {
        // 리뷰 확인
        Review review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_REVIEW));

        // 사용자 권한 확인 (예: 해당 리뷰 작성자만 신고 가능하도록 할 경우)
        if (!review.getUser().getUserId().equals(userId)) {
            throw new DeliveryApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        // 신고 처리
        review.setIsReported(true);
        review.setReportMessage(reportRequest.getReportMessage());

        // 트랜잭션이 완료된 후 캐시 무효화 수행
        clearAverageRatingCache(review.getStore().getStoreId());
        return reviewRepository.save(review);  // 저장 후 변경된 리뷰 반환
    }

    @Cacheable(value = "averageRatingCache", key = "#storeId")
    public Double calculateAverageRating(UUID storeId) {
        existStoreId(storeId);

        List<Review> reviews = reviewRepository.findByStore_StoreIdAndIsReportedFalse(storeId);

        if (reviews.isEmpty()) {
            return 0.0;
        }

        double sumRating = reviews.stream().mapToDouble(Review::getRating).sum();
        return sumRating / reviews.size();
    }


    private void existStoreId(UUID storeId) {
        if (!storeRepository.existsById(storeId)) {
            throw new DeliveryApplicationException(ErrorCode.NOT_FOUND_STORE);
        }
    }


}


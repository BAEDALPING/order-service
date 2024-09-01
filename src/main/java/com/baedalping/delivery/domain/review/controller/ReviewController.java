package com.baedalping.delivery.domain.review.controller;

import com.baedalping.delivery.domain.review.dto.ReportReviewRequestDto;
import com.baedalping.delivery.domain.review.dto.ReviewRequestDto;
import com.baedalping.delivery.domain.review.dto.ReviewResponseDto;
import com.baedalping.delivery.domain.review.entity.Review;
import com.baedalping.delivery.domain.review.service.ReviewService;
import com.baedalping.delivery.domain.store.dto.StoreResponseDto;
import com.baedalping.delivery.domain.user.dto.UserDetailsImpl;
import com.baedalping.delivery.global.common.ApiResponse;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/reviews")
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    @PostMapping
    public ApiResponse<ReviewResponseDto> createReview(
        @RequestBody @Valid ReviewRequestDto reviewRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        Long userId = userDetails.getUserId();
        Review review = reviewService.createReview(reviewRequest, userId);
        return ApiResponse.created(new ReviewResponseDto(review));
    }

    @GetMapping
    public ApiResponse<List<ReviewResponseDto>> getReviewsByStore(@RequestParam UUID storeId) {
        List<Review> reviews = reviewService.getReviewsByStore(storeId);
        List<ReviewResponseDto> response = reviews.stream()
            .map(ReviewResponseDto::new)
            .collect(Collectors.toList());

        return ApiResponse.ok(response);
    }

    // 리뷰 신고 API
    @PatchMapping("/{reviewId}/report")
    public ApiResponse<ReviewResponseDto> reportReview(
        @PathVariable UUID reviewId,
        @RequestBody @Valid ReportReviewRequestDto reportRequest,
        @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {

        Long userId = userDetails.getUserId();
        Review reportedReview = reviewService.reportReview(reviewId, reportRequest, userId);
        return ApiResponse.ok(new ReviewResponseDto(reportedReview));
    }


    // 특정 가게의 평균 평점 계산 API
    @GetMapping("/store/{storeId}/average-rating")
    public ApiResponse<Double> getAverageRatingByStore(@PathVariable UUID storeId) {
        Double averageRating = reviewService.calculateAverageRating(storeId);
        return ApiResponse.ok(averageRating);
    }

}


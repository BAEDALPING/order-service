package com.baedalping.delivery.domain.review.controller;

import com.baedalping.delivery.domain.review.dto.ReviewResponseDto;
import com.baedalping.delivery.domain.review.entity.Review;
import com.baedalping.delivery.domain.review.service.ReviewService;
import com.baedalping.delivery.global.common.ApiResponse;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/owner/reviews")
@RequiredArgsConstructor
public class OwnerReviewController {
    private final ReviewService reviewService;

    @GetMapping()
    public ApiResponse<List<ReviewResponseDto>> getAllReviewsByStore(
        @RequestParam UUID storeId) {

        List<Review> reviews = reviewService.getAllReviewsByStore(storeId);
        List<ReviewResponseDto> response = reviews.stream()
            .map(ReviewResponseDto::new)
            .collect(Collectors.toList());

        return ApiResponse.ok(response);
    }
}

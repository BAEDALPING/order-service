package com.baedalping.delivery.domain.review.dto;

import com.baedalping.delivery.domain.review.entity.Review;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewResponseDto {
    private UUID reviewId;
    private UUID orderId;
    private Long userId;
    private UUID storeId;
    private Integer rating;
    private String comment;

    public ReviewResponseDto(Review review) {
        this.reviewId = review.getReviewId();
        this.orderId = review.getOrder().getOrderId();
        this.userId = review.getUser().getUserId();
        this.storeId = review.getStore().getStoreId();
        this.rating = review.getRating();
        this.comment = review.getComment();
    }
}


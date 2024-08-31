package com.baedalping.delivery.domain.review.entity;

import com.baedalping.delivery.domain.store.entity.Store;
import com.baedalping.delivery.domain.user.entity.User;
import com.baedalping.delivery.domain.order.entity.Order;

import com.baedalping.delivery.global.common.AuditField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_reviews")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Review extends AuditField {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "review_id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID reviewId;

    @Setter
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false, columnDefinition = "UUID", unique = true)
    private Order order;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false, columnDefinition = "UUID")
    private Store store;

    @Setter
    @Column(name = "rating", nullable = false)
    private Integer rating;  // 1-5점 사이의 평점

    @Setter
    @Column(name = "comment", length = 1000)
    private String comment;  // 리뷰 내용

    @Setter
    @Column(name = "is_reported", nullable = false)
    private Boolean isReported = false;  // 신고 여부

    @Setter
    @Column(name = "report_message", length = 1000)
    private String reportMessage;  // 신고 메시지
}


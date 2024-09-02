package com.baedalping.delivery.domain.order.entity;

import com.baedalping.delivery.domain.payment.entity.Payment;
import com.baedalping.delivery.domain.review.entity.Review;
import com.baedalping.delivery.domain.store.entity.Store;
import com.baedalping.delivery.domain.user.entity.User;
import com.baedalping.delivery.global.common.AuditField;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_orders")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class Order extends AuditField {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID orderId;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false) // JoinColumn을 통해 외래 키 설정
    private User user;

    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false, columnDefinition = "UUID") // JoinColumn을 통해 외래 키 설정
    private Store store;

    @Setter
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment; // Payment와의 One-to-One 관계 설정

    @Setter
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Review review;

    @Setter
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderDetail> orderDetails;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 20, nullable = false)
    private OrderStatus state;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(name = "order_type", length = 20, nullable = false)
    private OrderType orderType;

    @Setter
    @Column(name = "total_quantity", nullable = false)
    private Integer totalQuantity;

    @Setter
    @Column(name = "total_price", nullable = false)
    private Integer totalPrice;

    @Column(name = "shipping_address", length = 100, nullable = false)
    private String shippingAddress;

    @Column(name = "is_public", nullable = false)
    private Boolean isPublic = true;
}

package com.baedalping.delivery.order.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID orderId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "store_id", columnDefinition = "UUID", nullable = false)
    private UUID storeId;

//    @Column(name = "order_date", nullable = false)
//    private LocalDateTime orderDate;

    @Setter
    @Column(name = "state", length = 20, nullable = false)
    private OrderStatus state;

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

    // TODO: Audit 설정 이후 기능 테스트

    //    @Column(name = "created_at", nullable = false, updatable = false)
//    private LocalDateTime createdAt;
//
//    @Column(name = "created_by", length = 100, nullable = false)
//    private String createdBy;
//
//    @Column(name = "updated_at")
//    private LocalDateTime updatedAt;
//
//    @Column(name = "updated_by", length = 100)
//    private String updatedBy;
//
//    @Column(name = "deleted_at")
//    private LocalDateTime deletedAt;
//
//    @Column(name = "deleted_by", length = 100)
//    private String deletedBy;


}

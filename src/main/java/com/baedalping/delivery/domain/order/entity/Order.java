package com.baedalping.delivery.domain.order.entity;

import com.baedalping.delivery.global.common.AuditField;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "store_id", columnDefinition = "UUID", nullable = false)
    private UUID storeId;

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

package com.baedalping.delivery.order.entity;

import com.baedalping.delivery.global.common.AuditField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_order_details")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class OrderDetail extends AuditField {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "order_detail_id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID orderDetailId;

    // 주문과의 N:1 관계 설정
    @Setter
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "product_id", columnDefinition = "UUID", nullable = false)
    private UUID productId;

    @Column(name = "product_name", length = 255, nullable = false)
    private String productName;

    @Column(name = "quantity", nullable = false)
    private Integer quantity;

    @Column(name = "unit_price",  nullable = false)
    private Integer unitPrice;

    @Setter
    @Column(name = "subtotal",  nullable = false)
    private Integer subtotal;


}

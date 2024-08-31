package com.baedalping.delivery.domain.payment.entity;

import com.baedalping.delivery.domain.order.entity.Order;
import com.baedalping.delivery.global.common.AuditField;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "p_payment")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class Payment extends AuditField {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "payment_id", updatable = false, nullable = false, columnDefinition = "UUID")
    private UUID paymentId;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order; // Order와의 One-to-One 관계 설정

    @Column(length = 100)
    private String paymentMethod;

    @Column(length = 255)
    private String cardNumber;

    @Setter
    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PaymentState state;

    @Column(precision = 10, scale = 2)
    private int totalAmount;

    private LocalDateTime paymentDate;

    @Column(nullable = false)
    private Boolean isPublic = true;

    public void setInvisible() {
        this.isPublic = false;
    }
}

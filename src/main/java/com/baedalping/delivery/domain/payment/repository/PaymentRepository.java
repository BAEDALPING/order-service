package com.baedalping.delivery.domain.payment.repository;


import com.baedalping.delivery.domain.payment.entity.Payment;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}


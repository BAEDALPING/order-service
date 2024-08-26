package com.baedalping.delivery.order.repository;

import com.baedalping.delivery.order.entity.OrderDetail;
import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {
    List<OrderDetail> findByOrderId(UUID orderId);
}

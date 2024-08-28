package com.baedalping.delivery.domain.order.repository;

import com.baedalping.delivery.domain.order.entity.Order;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {

    Page<Order> findByStoreIdAndIsPublicTrue(UUID storeId, Pageable pageable);

    Page<Order> findByUserIdAndIsPublicTrue(Long userId, Pageable pageable);
}

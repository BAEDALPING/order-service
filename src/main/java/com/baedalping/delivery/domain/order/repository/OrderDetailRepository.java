package com.baedalping.delivery.domain.order.repository;

import com.baedalping.delivery.domain.order.entity.OrderDetail;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderDetailRepository extends JpaRepository<OrderDetail, UUID> {

}

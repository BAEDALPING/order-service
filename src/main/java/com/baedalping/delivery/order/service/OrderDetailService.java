package com.baedalping.delivery.order.service;

import com.baedalping.delivery.order.entity.OrderDetail;
import com.baedalping.delivery.order.repository.OrderDetailRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    // TODO: batch insert 사용으로 변경
    @Transactional
    public void saveOrderDetails(UUID orderId, List<OrderDetail> orderDetails) {
        for (OrderDetail detail : orderDetails) {
            OrderDetail orderDetail = OrderDetail.builder()
                .orderId(orderId)
                .productId(detail.getProductId())
                .productName(detail.getProductName())
                .quantity(detail.getQuantity())
                .unitPrice(detail.getUnitPrice())
                .subtotal(detail.getQuantity() * detail.getUnitPrice())
                .build();

            orderDetailRepository.save(orderDetail);
        }
    }
}

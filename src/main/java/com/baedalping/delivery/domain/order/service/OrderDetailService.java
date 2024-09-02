package com.baedalping.delivery.domain.order.service;

import com.baedalping.delivery.domain.order.entity.OrderDetail;
import com.baedalping.delivery.domain.order.repository.OrderDetailRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;

    public void saveOrderDetails(List<OrderDetail> orderDetails) {
        orderDetailRepository.saveAll(orderDetails);
    }
}

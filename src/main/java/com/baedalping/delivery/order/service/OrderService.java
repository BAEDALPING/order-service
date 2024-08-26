package com.baedalping.delivery.order.service;

import com.baedalping.delivery.order.entity.Order;
import com.baedalping.delivery.order.entity.OrderDetail;
import com.baedalping.delivery.order.entity.OrderStatus;
import com.baedalping.delivery.order.repository.OrderRepository;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;

    public Order createOrder(Order order, List<OrderDetail> orderDetails) {
        int totalQuantity = 0;
        int totalPrice = 0;

        for (OrderDetail orderDetail : orderDetails) {
            totalQuantity += orderDetail.getQuantity();
            totalPrice += orderDetail.getQuantity() * orderDetail.getUnitPrice();
        }

        // 1. 주문 생성 및 저장
        order.setState(OrderStatus.PENDING);  // Enum으로 상태 관리
        order.setTotalQuantity(totalQuantity);
        order.setTotalPrice(totalPrice);
        Order savedOrder = orderRepository.save(order);

        // 2. 주문 상세 정보 저장
        orderDetailService.saveOrderDetails(savedOrder.getOrderId(), orderDetails);

        return savedOrder;
    }

    public List<Order> getOrdersByStoreId(UUID storeId) {
        return null;
    }

    public List<Order> getOrdersByUserId(Long userId) {
        return null;
    }

    public List<Order> searchOrders(String keyword) {
        return null;
    }

    public Order getOrderById(UUID orderId) {
        return null;
    }

    public Order cancelOrder(UUID orderId) {
        return null;
    }
}

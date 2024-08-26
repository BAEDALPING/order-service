package com.baedalping.delivery.order.dto;

import com.baedalping.delivery.order.entity.Order;
import com.baedalping.delivery.order.entity.OrderDetail;
import java.util.List;

public class OrderCreateRequestDto {
    private Order order;
    // TODO: 주문 상세 내역을 Redis에서 가져오도록 한 후 dto 수정
    private List<OrderDetail> orderDetails;

    // Getters and Setters
    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }
}

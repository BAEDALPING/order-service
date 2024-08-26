package com.baedalping.delivery.order.service;

import com.baedalping.delivery.order.dto.OrderDTO;
import com.baedalping.delivery.order.dto.OrderDetailDTO;
import com.baedalping.delivery.order.entity.Order;
import com.baedalping.delivery.order.entity.OrderDetail;
import com.baedalping.delivery.order.entity.OrderStatus;
import com.baedalping.delivery.order.repository.OrderRepository;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;

    @Transactional
    public OrderDTO createOrder(Order order, List<OrderDetail> orderDetails) {
        int totalQuantity = 0;
        int totalPrice = 0;

        for (OrderDetail orderDetail : orderDetails) {
            int subtotal = orderDetail.getQuantity() * orderDetail.getUnitPrice();
            orderDetail.setSubtotal(subtotal);

            totalQuantity += orderDetail.getQuantity();
            totalPrice += subtotal;
            orderDetail.setOrder(order);
        }

        order.setState(OrderStatus.PENDING);
        order.setTotalQuantity(totalQuantity);
        order.setTotalPrice(totalPrice);
        order.setOrderDetails(orderDetails);

        Order savedOrder = orderRepository.save(order);
        orderDetailService.saveOrderDetails(orderDetails);

        return convertToOrderDTO(savedOrder);
    }

    public OrderDTO getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
        return convertToOrderDTO(order);
    }

    private OrderDTO convertToOrderDTO(Order order) {
        OrderDTO orderDTO = new OrderDTO();
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setUserId(order.getUserId());
        orderDTO.setStoreId(order.getStoreId());
        orderDTO.setState(order.getState());
        orderDTO.setTotalQuantity(order.getTotalQuantity());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setShippingAddress(order.getShippingAddress());
        orderDTO.setIsPublic(order.getIsPublic());

        List<OrderDetailDTO> orderDetailDTOs = order.getOrderDetails().stream()
            .map(this::convertToOrderDetailDTO)
            .collect(Collectors.toList());
        orderDTO.setOrderDetails(orderDetailDTOs);

        return orderDTO;
    }

    private OrderDetailDTO convertToOrderDetailDTO(OrderDetail orderDetail) {
        OrderDetailDTO orderDetailDTO = new OrderDetailDTO();
        orderDetailDTO.setOrderDetailId(orderDetail.getOrderDetailId());
        orderDetailDTO.setProductId(orderDetail.getProductId());
        orderDetailDTO.setProductName(orderDetail.getProductName());
        orderDetailDTO.setQuantity(orderDetail.getQuantity());
        orderDetailDTO.setUnitPrice(orderDetail.getUnitPrice());
        orderDetailDTO.setSubtotal(orderDetail.getSubtotal());
        return orderDetailDTO;
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


    public Order cancelOrder(UUID orderId) {
        return null;
    }
}

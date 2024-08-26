package com.baedalping.delivery.order.service;

import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import com.baedalping.delivery.order.dto.OrderCreateResponseDto;
import com.baedalping.delivery.order.dto.OrderDetailResponseDto;
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
    public OrderCreateResponseDto createOrder(Order order, List<OrderDetail> orderDetails) {
        // 1. 유효성 검사
        validateOrderAndDetails(order, orderDetails);

        int totalQuantity = 0;
        int totalPrice = 0;

        // 2. 주문 상세 정보 처리 및 총계 계산
        for (OrderDetail orderDetail : orderDetails) {
            int subtotal = orderDetail.getQuantity() * orderDetail.getUnitPrice();
            orderDetail.setSubtotal(subtotal);

            totalQuantity += orderDetail.getQuantity();
            totalPrice += subtotal;
            orderDetail.setOrder(order);
        }

        // 3. 주문 정보 설정
        order.setState(OrderStatus.PENDING);
        order.setTotalQuantity(totalQuantity);
        order.setTotalPrice(totalPrice);
        order.setOrderDetails(orderDetails);

        // 4. 데이터베이스에 저장 및 예외 처리
        try {
            Order savedOrder = orderRepository.save(order);
            orderDetailService.saveOrderDetails(orderDetails);
            return convertToOrderDTO(savedOrder);
        } catch (Exception e) {
            throw new DeliveryApplicationException(ErrorCode.NOT_FOUND_ORDER);
        }
    }

    // 유효성 검사 메서드 분리
    private void validateOrderAndDetails(Order order, List<OrderDetail> orderDetails) {
        if (order == null || orderDetails == null || orderDetails.isEmpty()) {
            throw new DeliveryApplicationException(ErrorCode.NOT_FOUND_ORDER);
        }
    }



//    public OrderCreateResponseDto getOrderById(UUID orderId) {
//        Order order = orderRepository.findById(orderId)
//            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
//        return convertToOrderDTO(order);
//    }

    private OrderCreateResponseDto convertToOrderDTO(Order order) {
        OrderCreateResponseDto orderDTO = new OrderCreateResponseDto();
        orderDTO.setOrderId(order.getOrderId());
        orderDTO.setUserId(order.getUserId());
        orderDTO.setStoreId(order.getStoreId());
        orderDTO.setState(order.getState());
        orderDTO.setTotalQuantity(order.getTotalQuantity());
        orderDTO.setTotalPrice(order.getTotalPrice());
        orderDTO.setShippingAddress(order.getShippingAddress());
        orderDTO.setIsPublic(order.getIsPublic());

        List<OrderDetailResponseDto> orderDetailDTOs = order.getOrderDetails().stream()
            .map(this::convertToOrderDetailDTO)
            .collect(Collectors.toList());
        orderDTO.setOrderDetails(orderDetailDTOs);

        return orderDTO;
    }

    private OrderDetailResponseDto convertToOrderDetailDTO(OrderDetail orderDetail) {
        OrderDetailResponseDto orderDetailDTO = new OrderDetailResponseDto();
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

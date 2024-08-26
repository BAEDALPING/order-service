package com.baedalping.delivery.order.controller;

import com.baedalping.delivery.order.dto.OrderCreateRequestDto;
import com.baedalping.delivery.order.dto.OrderDTO;
import com.baedalping.delivery.order.entity.Order;
import com.baedalping.delivery.order.entity.OrderDetail;
import com.baedalping.delivery.order.service.OrderService;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    /*
    TODO: body로 받아오고 있는 주문 상세 내역을 Redis에서 가져오도록 변경할 것
    TODO: return 객체 공통 dto로 변경
     */
    @PostMapping
    public OrderDTO createOrder(@RequestBody OrderCreateRequestDto orderRequest) {
        Order order = orderRequest.getOrder();
        List<OrderDetail> orderDetails = orderRequest.getOrderDetails();
        return orderService.createOrder(order, orderDetails);
    }

    // 가게 주문 조회
//    @GetMapping("/store/{storeId}")
//    public List<Order> getOrdersByStore(@PathVariable UUID storeId) {
//        return orderService.getOrdersByStoreId(storeId);
//    }
//
//    // 개인 주문 조회
//    @GetMapping("/user/{userId}")
//    public List<Order> getOrdersByUser(@PathVariable Long userId) {
//        return orderService.getOrdersByUserId(userId);
//    }
//
//    // 주문 키워드 검색
//    @GetMapping("/search")
//    public List<Order> searchOrders(@RequestParam String keyword) {
//        return orderService.searchOrders(keyword);
//    }
//
//    // 주문 단건 상세 조회
//    @GetMapping("/{orderId}")
//    public Order getOrderById(@PathVariable UUID orderId) {
//        return orderService.getOrderById(orderId);
//    }
//
//    // 주문 취소
//    @PostMapping("/{orderId}/cancel")
//    public Order cancelOrder(@PathVariable UUID orderId) {
//        return orderService.cancelOrder(orderId);
//    }
}

package com.baedalping.delivery.domain.order.service;

import com.baedalping.delivery.domain.cart.service.CartService;
import com.baedalping.delivery.domain.order.dto.OrderCreateResponseDto;
import com.baedalping.delivery.domain.order.dto.OrderDetailResponseDto;
import com.baedalping.delivery.domain.order.dto.OrderGetResponseDto;
import com.baedalping.delivery.domain.order.entity.Order;
import com.baedalping.delivery.domain.order.entity.OrderDetail;
import com.baedalping.delivery.domain.order.entity.OrderStatus;
import com.baedalping.delivery.domain.order.repository.OrderRepository;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderDetailService orderDetailService;
    private final CartService cartService;
    private final OrderMapperService orderMapperService;


    // Authentication 적용 전 임시 userId
    private final Long userId = 1L;

    @Transactional
    public OrderCreateResponseDto createOrder(UUID addressID) {
        Map<String, Integer> orderDetailList = fetchCartItems(); // 장바구니 내용물 가져오기
        Order order = buildOrder(orderDetailList, addressID); // Order 엔티티 생성
        saveOrder(order); // Order 저장

        List<OrderDetail> orderDetails = createAndSaveOrderDetails(order,
            orderDetailList); // OrderDetail 엔티티 생성 및 저장
        List<OrderDetailResponseDto> orderDetailResponseDtos = orderMapperService.convertList(
            orderDetails, OrderDetailResponseDto.class); // 매핑 메서드 사용

        clearCart(); // 장바구니 비우기

        return orderMapperService.convert(order, OrderCreateResponseDto.class)
            .setOrderDetails(orderDetailResponseDtos); // OrderCreateResponseDto 반환
    }

    @Transactional(readOnly = true)
    public Page<OrderGetResponseDto> getOrdersByStoreId(
        UUID storeId, int page, int size, String sortDirection
    ) {
        Sort sort = createSort(sortDirection);

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Order> orders = orderRepository.findByStoreIdAndIsPublicTrue(storeId, pageable);
        return orderMapperService.convertPage(orders, OrderGetResponseDto.class);
    }

    @Transactional(readOnly = true)
    public Page<OrderGetResponseDto> getOrdersByUserId(
        Long userId, int page, int size, String sortDirection
    ) {
        Sort sort = createSort(sortDirection);

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Order> orders = orderRepository.findByUserIdAndIsPublicTrue(userId, pageable);
        return orderMapperService.convertPage(orders, OrderGetResponseDto.class);
    }

    @Transactional(readOnly = true)
    public OrderGetResponseDto getOrderById(UUID orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_ORDER));
        return orderMapperService.convert(order, OrderGetResponseDto.class);
    }

    private void validateAddress(UUID addressID) {
        // TODO: 검증된 userId를 통해 addressId 체크
    }

    private Map<String, Integer> fetchCartItems() {
        // 장바구니 내용물 가져오기
        Map<String, Integer> orderDetailList = cartService.getCartProducts(userId);
        if (orderDetailList.isEmpty()) {
            throw new DeliveryApplicationException(ErrorCode.NOT_FOUND_PRODUCT_IN_CART);
        }
        return orderDetailList;
    }

    private Order buildOrder(Map<String, Integer> orderDetailList, UUID addressID) {
        return Order.builder()
            .orderDate(LocalDateTime.now())
            .userId(userId)
            .storeId(extractStoreId(orderDetailList))  // 장바구니에서 storeId 추출하는 메서드
            .state(OrderStatus.PENDING)
            .totalQuantity(calculateTotalQuantity(orderDetailList))  // 총 수량 계산하는 메서드
            .totalPrice(calculateTotalPrice(orderDetailList))  // 총 가격 계산하는 메서드
            .shippingAddress(getAddressById(addressID))  // 주소 ID로 주소를 가져오는 메서드
            .isPublic(true)
            .build();
    }

    private void saveOrder(Order order) {
        // Order 엔티티 저장
        orderRepository.save(order);
    }

    private List<OrderDetail> createAndSaveOrderDetails(Order order,
        Map<String, Integer> orderDetailList) {
        List<OrderDetail> orderDetails = createOrderDetails(order, orderDetailList);
        order.setOrderDetails(orderDetails);
        orderDetailService.saveOrderDetails(orderDetails);
        return orderDetails;
    }


    private void clearCart() {
        cartService.clearCart(String.valueOf(userId));
    }


    private UUID extractStoreId(Map<String, Integer> orderDetailList) {
        // 장바구니의 첫 번째 상품에서 storeId 추출 (예시)
        String firstKey = orderDetailList.keySet().iterator().next();
        String storeIdStr = firstKey.split(":")[0];
        return UUID.fromString(storeIdStr);
    }

    private int calculateTotalQuantity(Map<String, Integer> orderDetailList) {
        return orderDetailList.values().stream().mapToInt(Integer::intValue).sum();
    }

    private int calculateTotalPrice(Map<String, Integer> orderDetailList) {
        // TODO: Product와의 연계 이후 실제 가격으로 구현
        // 총 가격 계산 로직 (예시)
        int totalPrice = 0;
        for (Map.Entry<String, Integer> entry : orderDetailList.entrySet()) {
            int quantity = entry.getValue();
            // 각 상품의 unitPrice를 가져오는 로직 필요 (예: productService.getProductPrice(productId))
            int unitPrice = 100;  // 예시로 100으로 설정, 실제로는 제품의 가격을 가져와야 함
            totalPrice += quantity * unitPrice;
        }
        return totalPrice;
    }

    private String getAddressById(UUID addressID) {
        // TODO: 주소 ID로부터 실제 주소 문자열을 가져오는 로직 (예시)
        return "Example Address";
    }

    private List<OrderDetail> createOrderDetails(Order order,
        Map<String, Integer> orderDetailList) {
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : orderDetailList.entrySet()) {
            String[] keys = entry.getKey().split(":");
            UUID productId = UUID.fromString(keys[1]);
            String productName = "Example Product Name";  // TODO: 실제 제품명을 가져오는 로직 필요
            int quantity = entry.getValue();
            int unitPrice = 100;  //TODO: 실제 가격을 가져오는 로직 필요
            int subtotal = quantity * unitPrice;

            OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .productId(productId)
                .productName(productName)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .subtotal(subtotal)
                .build();

            orderDetails.add(orderDetail);
        }

        return orderDetails;
    }

    private Sort createSort(String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection); // "asc" or "desc"
        return Sort.by(
            new Sort.Order(direction, "createdAt"),
            new Sort.Order(direction, "updatedAt")
        );
    }


//    public OrderCreateResponseDto getOrderById(UUID orderId) {
//        Order order = orderRepository.findById(orderId)
//            .orElseThrow(() -> new IllegalArgumentException("Order not found"));
//        return convertToOrderDTO(order);
//    }

//    public List<Order> getOrdersByStoreId(UUID storeId) {
//        return null;
//    }
//
//    public List<Order> getOrdersByUserId(Long userId) {
//        return null;
//    }
//
//    public List<Order> searchOrders(String keyword) {
//        return null;
//    }
//
//
//    public Order cancelOrder(UUID orderId) {
//        return null;
//    }
}

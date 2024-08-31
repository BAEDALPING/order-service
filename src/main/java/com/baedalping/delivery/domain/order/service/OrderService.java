package com.baedalping.delivery.domain.order.service;

import com.baedalping.delivery.domain.cart.service.CartService;
import com.baedalping.delivery.domain.order.dto.OrderCreateResponseDto;
import com.baedalping.delivery.domain.order.dto.OrderDetailResponseDto;
import com.baedalping.delivery.domain.order.dto.OrderGetResponseDto;
import com.baedalping.delivery.domain.order.entity.Order;
import com.baedalping.delivery.domain.order.entity.OrderDetail;
import com.baedalping.delivery.domain.order.entity.OrderStatus;
import com.baedalping.delivery.domain.order.entity.OrderType;
import com.baedalping.delivery.domain.order.repository.OrderRepository;
import com.baedalping.delivery.domain.product.entity.Product;
import com.baedalping.delivery.domain.product.repository.ProductRepository;
import com.baedalping.delivery.domain.store.entity.Store;
import com.baedalping.delivery.domain.store.repository.StoreRepository;
import com.baedalping.delivery.domain.user.entity.User;
import com.baedalping.delivery.domain.user.entity.UserAddress;
import com.baedalping.delivery.domain.user.repository.UserAddressRepository;
import com.baedalping.delivery.domain.user.repository.UserRepository;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import java.time.Duration;
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
    private final ProductRepository productRepository;
    private final StoreRepository storeRepository;
    private final UserRepository userRepository;
    private final UserAddressRepository userAddressRepository;

    @Transactional
    public OrderCreateResponseDto createOrder(Long userId, UUID addressID, OrderType orderType) {
        // 1. 주소와 사용자 검증
        validateAddress(userId, addressID);

        Map<String, Integer> orderDetailList = fetchCartItems(userId);
        Order order = buildOrder(orderDetailList, userId, addressID, orderType);
        saveOrder(order);

        List<OrderDetail> orderDetails = createAndSaveOrderDetails(order, orderDetailList);
        List<OrderDetailResponseDto> orderDetailResponseDtos = orderMapperService.convertList(orderDetails, OrderDetailResponseDto.class);

        clearCart(userId);

        return orderMapperService.convert(order, OrderCreateResponseDto.class)
            .setOrderDetails(orderDetailResponseDtos);
    }

    @Transactional(readOnly = true)
    public Page<OrderGetResponseDto> getOrdersByStoreId(UUID storeId, int page, int size, String sortDirection) {
        Sort sort = createSort(sortDirection);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Order> orders = orderRepository.findByStore_StoreIdAndIsPublicTrue(storeId, pageable);
        return orderMapperService.convertPage(orders, OrderGetResponseDto.class);
    }

    // 2. 사용자 주문 조회 메서드
    @Transactional(readOnly = true)
    public Page<OrderGetResponseDto> getOrdersByUserId(Long userId, int page, int size, String sortDirection) {
        Sort sort = createSort(sortDirection);
        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Order> orders = orderRepository.findByUser_UserIdAndIsPublicTrue(userId, pageable);
        return orderMapperService.convertPage(orders, OrderGetResponseDto.class);
    }

    // 3. 단일 주문 조회 메서드
    @Transactional(readOnly = true)
    public OrderGetResponseDto getOrderById(UUID orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_ORDER));

        // 사용자가 해당 주문의 소유자인지 확인
        if (!order.getUser().getUserId().equals(userId)) {
            throw new DeliveryApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        return orderMapperService.convert(order, OrderGetResponseDto.class);
    }

    private void validateAddress(Long userId, UUID addressID) {
        UserAddress address = userAddressRepository.findById(addressID)
            .orElseThrow(() -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_USER_ADDRESS));

        if (!address.getUser().getUserId().equals(userId)) {
            throw new DeliveryApplicationException(ErrorCode.USER_ADDRESS_MISMATCH);
        }
    }

    private Map<String, Integer> fetchCartItems(Long userId) {
        Map<String, Integer> orderDetailList = cartService.getCartProducts(userId);
        if (orderDetailList.isEmpty()) {
            throw new DeliveryApplicationException(ErrorCode.NOT_FOUND_PRODUCT_IN_CART);
        }
        return orderDetailList;
    }

    private Order buildOrder(Map<String, Integer> orderDetailList, Long userId, UUID addressID, OrderType orderType) {
        return Order.builder()
            .orderDate(LocalDateTime.now())
            .user(getUser(userId))
            .orderType(orderType)
            .store(extractStoreId(orderDetailList))
            .state(OrderStatus.PENDING)
            .totalQuantity(calculateTotalQuantity(orderDetailList))
            .totalPrice(calculateTotalPrice(orderDetailList))
            .shippingAddress(getAddressById(addressID))
            .isPublic(true)
            .build();
    }

    private void saveOrder(Order order) {
        orderRepository.save(order);
    }

    private List<OrderDetail> createAndSaveOrderDetails(Order order, Map<String, Integer> orderDetailList) {
        List<OrderDetail> orderDetails = createOrderDetails(order, orderDetailList);
        order.setOrderDetails(orderDetails);
        orderDetailService.saveOrderDetails(orderDetails);
        return orderDetails;
    }

    private void clearCart(Long userId) {
        cartService.clearCart(String.valueOf(userId));
    }

    private Store extractStoreId(Map<String, Integer> orderDetailList) {
        String firstKey = orderDetailList.keySet().iterator().next();
        String storeIdStr = firstKey.split(":")[0];

        return storeRepository.findById(UUID.fromString(storeIdStr)).orElseThrow(
            () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_STORE)
        );
    }

    private int calculateTotalQuantity(Map<String, Integer> orderDetailList) {
        return orderDetailList.values().stream().mapToInt(Integer::intValue).sum();
    }

    private int calculateTotalPrice(Map<String, Integer> orderDetailList) {
        int totalPrice = 0;
        for (Map.Entry<String, Integer> entry : orderDetailList.entrySet()) {
            int quantity = entry.getValue();
            UUID productId = UUID.fromString(entry.getKey().split(":")[1]);

            Product product = productRepository.findById(productId)
                .orElseThrow(() -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_PRODUCT));
            int unitPrice = product.getProductPrice();
            totalPrice += quantity * unitPrice;
        }
        return totalPrice;
    }

    private String getAddressById(UUID addressID) {
        UserAddress address = userAddressRepository.findById(addressID).orElseThrow(
            () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_USER_ADDRESS)
        );
        return address.getAddress();
    }

    private List<OrderDetail> createOrderDetails(Order order, Map<String, Integer> orderDetailList) {
        List<OrderDetail> orderDetails = new ArrayList<>();

        for (Map.Entry<String, Integer> entry : orderDetailList.entrySet()) {
            String[] keys = entry.getKey().split(":");
            UUID productId = UUID.fromString(keys[1]);

            Product product = fetchProductById(productId);
            String productName = product.getProductName();
            int quantity = entry.getValue();
            int unitPrice = product.getProductPrice();
            int subtotal = quantity * unitPrice;

            OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)
                .productName(productName)
                .quantity(quantity)
                .unitPrice(unitPrice)
                .subtotal(subtotal)
                .build();

            orderDetails.add(orderDetail);
        }

        return orderDetails;
    }

    private Product fetchProductById(UUID productId) {
        return productRepository.findById(productId).orElseThrow(
            () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_PRODUCT)
        );
    }

    private Sort createSort(String sortDirection) {
        Sort.Direction direction = Sort.Direction.fromString(sortDirection);
        return Sort.by(new Sort.Order(direction, "createdAt"), new Sort.Order(direction, "updatedAt"));
    }

    public Page<OrderGetResponseDto> searchOrdersByKeyword(String keyword, int page, int size, String sortDirection) {
        Sort sort = createSort(sortDirection);
        Pageable pageable = PageRequest.of(page, size, sort);
        return orderMapperService.convertPage(orderRepository.findOrdersByKeyword(keyword, pageable), OrderGetResponseDto.class);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_USER)
        );
    }

    @Transactional
    public OrderGetResponseDto cancelOrder(UUID orderId, Long userId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_ORDER));

        if (!order.getUser().getUserId().equals(userId)) {
            throw new DeliveryApplicationException(ErrorCode.INVALID_PERMISSION);
        }

        LocalDateTime now = LocalDateTime.now();
        if (Duration.between(order.getOrderDate(), now).toMinutes() > 5) {
            throw new DeliveryApplicationException(ErrorCode.CANNOT_CANCEL_ORDER_AFTER_5_MINUTES);
        }

        order.setState(OrderStatus.CANCELLED);
        orderRepository.save(order);
        return orderMapperService.convert(order, OrderGetResponseDto.class);
    }
}


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


    // Authentication 적용 전 임시 userId
    private final Long userId = 1L;


    @Transactional
    public OrderCreateResponseDto createOrder(UUID addressID, OrderType orderType) {
        Map<String, Integer> orderDetailList = fetchCartItems(); // 장바구니 내용물 가져오기
        Order order = buildOrder(orderDetailList, addressID, orderType); // Order 엔티티 생성
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
        Page<Order> orders = orderRepository.findByStore_StoreIdAndIsPublicTrue(storeId, pageable);
        return orderMapperService.convertPage(orders, OrderGetResponseDto.class);
    }

    @Transactional(readOnly = true)
    public Page<OrderGetResponseDto> getOrdersByUserId(
        Long userId, int page, int size, String sortDirection
    ) {
        Sort sort = createSort(sortDirection);

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<Order> orders = orderRepository.findByUser_UserIdAndIsPublicTrue(userId, pageable);
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

    private Order buildOrder(Map<String, Integer> orderDetailList, UUID addressID,
        OrderType orderType) {
        return Order.builder()
            .orderDate(LocalDateTime.now())
            .user(getUser(userId))
            .orderType(orderType)
            .store(extractStoreId(orderDetailList))  // 장바구니에서 storeId 추출하는 메서드
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


    private Store extractStoreId(Map<String, Integer> orderDetailList) {
        // 장바구니의 첫 번째 상품에서 storeId 추출 (예시)
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

            // TODO: 실제 Product 객체를 가져오는 로직 필요
            Product product = fetchProductById(
                productId);  // productId를 사용하여 Product 객체를 가져오는 메서드 호출

            String productName = product.getProductName();  // Product 객체에서 제품명 가져오기
            int quantity = entry.getValue();
            int unitPrice = product.getProductPrice();  // Product 객체에서 단가 가져오기
            int subtotal = quantity * unitPrice;

            OrderDetail orderDetail = OrderDetail.builder()
                .order(order)
                .product(product)  // 변경: Product 객체로 설정
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
        Sort.Direction direction = Sort.Direction.fromString(sortDirection); // "asc" or "desc"
        return Sort.by(
            new Sort.Order(direction, "createdAt"),
            new Sort.Order(direction, "updatedAt")
        );
    }

    public Page<OrderGetResponseDto> searchOrdersByKeyword(String keyword,int page, int size, String sortDirection) {
        // TODO: 연관관계 수정 후 전체 재조정
        Sort sort = createSort(sortDirection);

        Pageable pageable = PageRequest.of(page, size, sort);
        return orderMapperService.convertPage(orderRepository.findOrdersByKeyword(keyword, pageable),
            OrderGetResponseDto.class);
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(
            () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_USER)
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
    @Transactional
    public OrderGetResponseDto cancelOrder(UUID orderId) {
        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_ORDER));

        // 주문 생성 후 5분 이내에만 취소 가능하도록 체크
        LocalDateTime now = LocalDateTime.now();
        if (Duration.between(order.getOrderDate(), now).toMinutes() > 5) {
            throw new DeliveryApplicationException(ErrorCode.CANNOT_CANCEL_ORDER_AFTER_5_MINUTES);
        }

        // 취소된 주문도 고객이 볼 수 있을것인가? (IsPublic을 false로 전환해야 하는가)
        order.setState(OrderStatus.CANCELLED);
        orderRepository.save(order);
        return orderMapperService.convert(order, OrderGetResponseDto.class);
    }
}

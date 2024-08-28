package com.baedalping.delivery;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.baedalping.delivery.domain.cart.dto.CartRequestDto;
import com.baedalping.delivery.domain.cart.service.CartService;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;

class CartServiceTest {

    // RedisTemplate을 모의(Mock) 객체로 생성하여 Redis와의 실제 상호작용을 피함
    @Mock
    private RedisTemplate<String, Object> redisTemplate;

    // HashOperations을 모의(Mock) 객체로 생성하여 해시 연산을 모의함
    @Mock
    private HashOperations<String, Object, Object> hashOperations;

    // CartService 객체를 테스트할 때 모의 객체를 주입받도록 설정
    @InjectMocks
    private CartService cartService;

    // 테스트에서 사용할 유저 ID와 Redis 키 생성
    private final String userId = "1";
    private final String cartKey = "cart:" + userId;
    private CartRequestDto cartProductDto;

    // 각 테스트 메서드 실행 전에 호출되어 테스트 환경을 설정
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this); // Mockito 어노테이션 초기화

        // 테스트에 사용할 CartProductDto 객체 생성
        cartProductDto = new CartRequestDto("store1", "product1", 2);

        // RedisTemplate의 opsForHash 메서드가 호출될 때 hashOperations 모의 객체를 반환하도록 설정
        when(redisTemplate.opsForHash()).thenReturn(hashOperations);
    }

    // 장바구니에 상품을 추가하는 기능을 테스트
    @Test
    void testAddProductToCart() {
        // Redis에서 장바구니가 비어있는 상태를 모의
        when(hashOperations.entries(cartKey)).thenReturn(new HashMap<>());

        // 장바구니에 상품을 추가
        cartService.addProductToCart(userId, cartProductDto);

        // HashOperations의 put 메서드가 올바른 매개변수와 함께 한 번 호출되었는지 검증
        verify(hashOperations, times(1)).put(cartKey, "store1:product1", 2);
    }

    // 장바구니에 이미 다른 가게의 상품이 있을 때 예외가 발생하는지 테스트
    @Test
    void testAddProductToCartInvalidStore() {
        // 다른 가게의 상품이 이미 장바구니에 있는 상태를 모의
        Map<Object, Object> cartItems = new HashMap<>();
        cartItems.put("store2:product2", 1);
        when(hashOperations.entries(cartKey)).thenReturn(cartItems);

        // 장바구니에 상품을 추가하려고 할 때 예외가 발생하는지 확인
        DeliveryApplicationException exception = assertThrows(DeliveryApplicationException.class,
            () -> cartService.addProductToCart(userId, cartProductDto));

        // 발생한 예외의 에러 코드가 INVALID_STORE인지 확인
        assertEquals(ErrorCode.CART_ONLY_ONE_STORE_ALLOWED, exception.getErrorCode());
    }

    // 장바구니에 있는 특정 상품의 수량을 변경하는 기능을 테스트
    @Test
    void testUpdateProductQuantity() {
        // 장바구니에 상품이 있는 상태를 모의
        Map<Object, Object> cartItems = new HashMap<>();
        cartItems.put("store1:product1", 2);
        when(hashOperations.keys(cartKey)).thenReturn(cartItems.keySet());

        // 상품 수량을 업데이트
        cartService.updateProductQuantity(userId, "product1", 5);

        // HashOperations의 put 메서드가 올바른 매개변수와 함께 한 번 호출되었는지 검증
        verify(hashOperations, times(1)).put(cartKey, "store1:product1", 5);
    }

    // 장바구니에 없는 상품의 수량을 변경하려고 할 때 예외가 발생하는지 테스트
    @Test
    void testUpdateProductQuantityItemNotFound() {
        // 장바구니에 상품이 없는 상태를 모의
        when(hashOperations.keys(cartKey)).thenReturn(Set.of());

        // 상품 수량을 변경하려고 할 때 예외가 발생하는지 확인
        DeliveryApplicationException exception = assertThrows(DeliveryApplicationException.class,
            () -> cartService.updateProductQuantity(userId, "product1", 5));

        // 발생한 예외의 에러 코드가 ITEM_NOT_FOUND_IN_CART인지 확인
        assertEquals(ErrorCode.NOT_FOUND_PRODUCT_IN_CART, exception.getErrorCode());
    }

    // 장바구니에서 특정 상품을 삭제하는 기능을 테스트
    @Test
    void testRemoveItemFromCart() {
        // 장바구니에 상품이 있는 상태를 모의
        Map<Object, Object> cartItems = new HashMap<>();
        cartItems.put("store1:product1", 2);
        when(hashOperations.keys(cartKey)).thenReturn(cartItems.keySet());

        // 상품을 장바구니에서 삭제
        cartService.removeItemFromCart(userId, "product1");

        // HashOperations의 delete 메서드가 올바른 매개변수와 함께 한 번 호출되었는지 검증
        verify(hashOperations, times(1)).delete(cartKey, "store1:product1");
    }

    // 장바구니에 없는 상품을 삭제하려고 할 때 예외가 발생하는지 테스트
    @Test
    void testRemoveItemFromCartItemNotFound() {
        // 장바구니에 상품이 없는 상태를 모의
        when(hashOperations.keys(cartKey)).thenReturn(Set.of());

        // 상품을 삭제하려고 할 때 예외가 발생하는지 확인
        DeliveryApplicationException exception = assertThrows(DeliveryApplicationException.class,
            () -> cartService.removeItemFromCart(userId, "product1"));

        // 발생한 예외의 에러 코드가 ITEM_NOT_FOUND_IN_CART인지 확인
        assertEquals(ErrorCode.NOT_FOUND_PRODUCT_IN_CART, exception.getErrorCode());
    }

    // 장바구니를 비우는 기능을 테스트
    @Test
    void testClearCart() {
        // 장바구니를 비움
        cartService.clearCart(userId);

        // RedisTemplate의 delete 메서드가 올바른 매개변수와 함께 한 번 호출되었는지 검증
        verify(redisTemplate, times(1)).delete(cartKey);
    }
}

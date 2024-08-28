package com.baedalping.delivery.cart.service;

import com.baedalping.delivery.cart.dto.CartRequestDto;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final String CART_PREFIX = "cart:"; // Redis 키 앞에 유저 ID를 붙이기 위한 접두사

    // 상품 추가
    public Map<String, Integer> addProductToCart(String userId, CartRequestDto cartProduct) {
        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        String cartKey = CART_PREFIX + userId;

        // 상품 키 생성 ("storeId:productId" 형식)
        String cartProductKey = getProductKey(cartProduct.getStoreId(), cartProduct.getProductId());

        // 장바구니에 다른 가게의 상품이 있는지 확인
        if (!hashOps.keys(cartKey).isEmpty()) {
            String existingStoreId = hashOps.keys(cartKey).iterator().next().split(":")[0];
            if (!existingStoreId.equals(cartProduct.getStoreId())) {
                throw new DeliveryApplicationException(ErrorCode.CART_ONLY_ONE_STORE_ALLOWED);
            }
        }

        // 기존 수량을 가져오고, 수량 업데이트
        Integer currentQuantity = hashOps.get(cartKey, cartProductKey);
        if (currentQuantity == null) {
            currentQuantity = 0;
        }
        hashOps.put(cartKey, cartProductKey, currentQuantity + cartProduct.getQuantity());

        return hashOps.entries(cartKey); // 현재 장바구니 상태를 반환
    }

    // 상품 수량 변경
    public Map<String, Integer> updateProductQuantity(String userId, String productId, int newQuantity) {
        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        String cartKey = CART_PREFIX + userId;
        String productKey = getProductKey(userId, productId);

        Integer currentQuantity = hashOps.get(cartKey, productKey);

        if (currentQuantity != null) {
            if (newQuantity > 0) {
                hashOps.put(cartKey, productKey, newQuantity);
            } else {
                hashOps.delete(cartKey, productKey);
            }
        } else {
            throw new DeliveryApplicationException(ErrorCode.NOT_FOUND_PRODUCT_IN_CART);
        }

        return hashOps.entries(cartKey);
    }

    // 상품 삭제
    public Map<String, Integer> removeItemFromCart(String userId, String productId) {
        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        String cartKey = CART_PREFIX + userId;
        String productKey = getProductKey(userId, productId);

        if (hashOps.hasKey(cartKey, productKey)) {
            hashOps.delete(cartKey, productKey);
        } else {
            throw new DeliveryApplicationException(ErrorCode.NOT_FOUND_PRODUCT_IN_CART);
        }

        return hashOps.entries(cartKey);
    }

    // 장바구니 비우기
    public Map<String, Integer> clearCart(String userId) {
        String cartKey = CART_PREFIX + userId;
        redisTemplate.delete(cartKey);
        return Map.of(); // 비어있는 장바구니 상태를 반환
    }

    // 헬퍼 메서드: Product Key 생성
    private String getProductKey(String storeId, String productId) {
        return storeId + ":" + productId;
    }

    public Map<String, Integer> getCartProducts(Long userId) {
        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        String cartKey = CART_PREFIX + userId;

        return hashOps.entries(cartKey);
    }
}






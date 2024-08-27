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
    // private final ProductService productService; // ProductService를 통해 상품 정보 가져오기
    private final String CART_PREFIX = "cart:"; // Redis 키 앞에 유저 ID를 붙이기 위한 접두사


    // 상품 추가
    public Map<String, Integer> addProductToCart(String userId, CartRequestDto cartProduct) {
        /*
         * TODO: 상품 검증 로직 추가
         *
         * 1. 상품 service와 통합 이후 진행
         * 2. dto를 통해 받아온 상품 ID와 가게 ID의 관계가 적절한지 = 해당 가게의 상품이 맞는지
         * 3. 상품 id를 통해 연관관계를 통해 storeId를 가져와 비교하는 방식 - 추후 변동 가능
         */

        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        String cartKey = CART_PREFIX + userId; // 유저별로 장바구니 관리

        // 이미 다른 가게의 상품이 장바구니에 있는지 확인
        Map<String, Integer> currentCartItems = hashOps.entries(cartKey);
        if (!currentCartItems.isEmpty()) {
            String existingStoreId = currentCartItems.keySet().iterator().next().split(":")[0];
            if (!existingStoreId.equals(cartProduct.getStoreId())) {
                throw new DeliveryApplicationException(ErrorCode.INVALID_STORE);
            }
        }

        // 상품 추가 (기존 상품이 있는 경우 수량을 증가)
        String cartItemKey = cartProduct.getStoreId() + ":" + cartProduct.getProductId();
        Integer currentQuantity = hashOps.get(cartKey, cartItemKey);
        if (currentQuantity == null) {
            currentQuantity = 0;
        }
        hashOps.put(cartKey, cartItemKey, currentQuantity + cartProduct.getQuantity());

        return hashOps.entries(cartKey); // 현재 장바구니 상태를 반환
    }

    // 장바구니 조회
//    public List<Product> getCartItems(String userId) {
//        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
//        String cartKey = CART_PREFIX + userId;
//        Map<String, Integer> cartItems = hashOps.entries(cartKey);
//
//        List<Product> products = new ArrayList<>();
//        for (Map.Entry<String, Integer> entry : cartItems.entrySet()) {
//            String[] keys = entry.getKey().split(":");
//            String storeId = keys[0];
//            String productId = keys[1];
//            Product product = productService.getProductById(productId); // ProductService를 통해 상품 정보 가져오기
//            product.setQuantity(entry.getValue());
//            products.add(product);
//        }
//        return products;
//    }

    // 상품 수량 변경
    public Map<String, Integer> updateProductQuantity(String userId, String productId,
        int newQuantity) {
        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        String cartKey = CART_PREFIX + userId;
        Set<String> keys = hashOps.keys(cartKey);
        boolean itemFound = false;

        for (String key : keys) {
            if (key.endsWith(":" + productId)) {
                if (newQuantity > 0) {
                    hashOps.put(cartKey, key, newQuantity);
                } else {
                    hashOps.delete(cartKey, key);
                }
                itemFound = true;
                break;
            }
        }

        if (!itemFound) {
            throw new DeliveryApplicationException(ErrorCode.ITEM_NOT_FOUND_IN_CART);
        }

        return hashOps.entries(cartKey); // 현재 장바구니 상태를 반환
    }

    // 상품 삭제
    public Map<String, Integer> removeItemFromCart(String userId, String productId) {
        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        String cartKey = CART_PREFIX + userId;

        // 해당 상품을 찾아서 삭제
        Set<String> keys = hashOps.keys(cartKey);
        boolean itemFound = false; // 상품이 존재하는지 여부를 체크하는 플래그

        for (String key : keys) {
            if (key.endsWith(":" + productId)) {
                hashOps.delete(cartKey, key);
                itemFound = true;
                break;
            }
        }

        // 장바구니에 해당 상품이 없을 경우 예외 발생
        if (!itemFound) {
            throw new DeliveryApplicationException(ErrorCode.ITEM_NOT_FOUND_IN_CART);
        }

        return hashOps.entries(cartKey); // 현재 장바구니 상태를 반환

    }


    // 장바구니 비우기
    public Map<String, Integer> clearCart(String userId) {
        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        String cartKey = CART_PREFIX + userId;

        redisTemplate.delete(CART_PREFIX + userId);

        return hashOps.entries(cartKey); // 현재 장바구니 상태를 반환
    }
}





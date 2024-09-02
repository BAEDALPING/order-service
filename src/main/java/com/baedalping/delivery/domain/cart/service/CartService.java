package com.baedalping.delivery.domain.cart.service;

import com.baedalping.delivery.domain.cart.dto.CartProductDto;
import com.baedalping.delivery.domain.cart.dto.CartRequestDto;
import com.baedalping.delivery.domain.cart.dto.ProductDto;
import com.baedalping.delivery.domain.product.entity.Product;
import com.baedalping.delivery.domain.product.repository.ProductRepository;
import com.baedalping.delivery.global.common.exception.DeliveryApplicationException;
import com.baedalping.delivery.global.common.exception.ErrorCode;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final ProductRepository productRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private final String CART_PREFIX = "cart:"; // Redis 키 앞에 유저 ID를 붙이기 위한 접두사

    // 상품 추가
    @Transactional
    public Map<String, Integer> addProductToCart(String userId, CartRequestDto cartProduct) {

        // 검증 서순 변경 가능
        Product product = productRepository.findById(UUID.fromString(cartProduct.getProductId())).orElseThrow(
            () -> new DeliveryApplicationException(ErrorCode.NOT_FOUND_PRODUCT)
        );

        if (!product.getStore().getStoreId().equals(UUID.fromString(cartProduct.getStoreId()))){
            throw new DeliveryApplicationException(ErrorCode.INVALID_PRODUCT_STORE_COMBINATION);
        }

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
    public Map<String, Integer> updateProductQuantity(String userId, CartRequestDto cartRequestDto) {
        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        String cartKey = CART_PREFIX + userId;
        String productKey = getProductKey(cartRequestDto.getStoreId(), cartRequestDto.getProductId());

        Integer currentQuantity = hashOps.get(cartKey, productKey);

        if (currentQuantity != null) {
            if (cartRequestDto.getQuantity() > 0) {
                hashOps.put(cartKey, productKey, cartRequestDto.getQuantity());
            } else {
                hashOps.delete(cartKey, productKey);
            }
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

    public List<CartProductDto> getCartItems(String userId) {
        HashOperations<String, String, Integer> hashOps = redisTemplate.opsForHash();
        String cartKey = CART_PREFIX + userId;

        // Redis에서 모든 상품 키와 수량을 가져옵니다.
        Map<String, Integer> cartEntries = hashOps.entries(cartKey);

        // Redis에 저장된 모든 productId를 추출합니다.
        List<UUID> productIds = cartEntries.keySet().stream()
            .map(productKey -> UUID.fromString(productKey.split(":")[1]))
            .collect(Collectors.toList());

        // 필요한 모든 상품을 한 번의 조회로 가져옵니다.
        List<Product> products = productRepository.findAllById(productIds);

        // 상품 정보를 맵으로 변환합니다. (productId를 키로 하는 맵)
        Map<UUID, Product> productMap = products.stream()
            .collect(Collectors.toMap(Product::getProductId, product -> product));

        // 상품 정보를 조회하고, 수량과 함께 DTO로 변환합니다.

        return cartEntries.entrySet().stream()
            .map(entry -> {
                String productKey = entry.getKey();
                Integer quantity = entry.getValue();

                // productKey로부터 productId를 추출합니다.
                UUID productId = UUID.fromString(productKey.split(":")[1]);

                // productMap에서 상품 정보를 가져옵니다.
                Product product = productMap.get(productId);
                if (product == null) {
                    throw new DeliveryApplicationException(ErrorCode.NOT_FOUND_PRODUCT);
                }

                // 정적 팩토리 메서드를 사용하여 Product를 ProductDto로 변환합니다.
                ProductDto productDto = ProductDto.fromEntity(product);

                // DTO로 변환하여 반환합니다.
                return new CartProductDto(productDto, quantity);
            })
            .collect(Collectors.toList());
    }
}






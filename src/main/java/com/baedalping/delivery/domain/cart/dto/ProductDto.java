package com.baedalping.delivery.domain.cart.dto;

import com.baedalping.delivery.domain.product.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import java.util.UUID;


@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDto {
    private UUID productId;
    private String productName;
    private Integer productPrice;
    private String productDetail;
    private String productImgUrl;

    // 정적 팩토리 메서드 추가
    public static ProductDto fromEntity(Product product) {
        return new ProductDto(
            product.getProductId(),
            product.getProductName(),
            product.getProductPrice(),
            product.getProductDetail(),
            product.getProductImgUrl()
        );
    }
}

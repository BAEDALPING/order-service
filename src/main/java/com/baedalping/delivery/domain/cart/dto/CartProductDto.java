package com.baedalping.delivery.domain.cart.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@NoArgsConstructor
@Getter
public class CartProductDto {
    private ProductDto product;
    private int quantity;

    public CartProductDto(ProductDto product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }
}

package com.baedalping.delivery.cart.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CartRequestDto {

    private String storeId;
    private String productId;
    private int quantity;

}

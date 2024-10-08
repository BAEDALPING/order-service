package com.baedalping.delivery.domain.cart.dto;

import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CartResponseDto {
    private Map<String, Integer> cartProducts;
}
